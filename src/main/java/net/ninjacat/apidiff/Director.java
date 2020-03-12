package net.ninjacat.apidiff;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.jar.JarFile;

import com.google.common.base.Strings;
import io.vavr.control.Try;
import net.ninjacat.apidiff.api.ApiJar;
import net.ninjacat.apidiff.logging.ConsoleLogger;
import net.ninjacat.apidiff.logging.Logger;
import net.ninjacat.apidiff.net.Downloader;
import org.immutables.value.Value;

import static net.ninjacat.apidiff.utils.FuncUtils.firstNonEmpty;
import static org.fusesource.jansi.Ansi.ansi;

public class Director {
  private static final Logger LOGGER = ConsoleLogger.getLogger();

  public void execute(final Args params) {
    final ComparisonArtifacts artifacts = usingLocalFiles(params)
                                          ? getLocalFiles(params)
                                          : downloadFromMaven(params);
    compareApis(artifacts);
  }

  private ComparisonArtifacts downloadFromMaven(final Args params) {
    final var currentCoordinates = paramsToSourceCoordinates(params);
    final var previousCoordinates = ImmutableCoordinates.copyOf(currentCoordinates)
                                                        .withVersion(params.getPreviousVersion());

    if (currentCoordinates.versionAsSemVer().compareTo(previousCoordinates.versionAsSemVer()) <= 0) {
      LOGGER.fail("Current version (%s) must be higher than previous version (%s)",
                  currentCoordinates.version(), previousCoordinates.version());
    }

    LOGGER.print(ansi().a("Comparing public API of ")
                       .fgBrightBlue().a(currentCoordinates.getJarName())
                       .fgDefault().a(" with ")
                       .fgBrightBlue().a(previousCoordinates.getJarName()).fgDefault().toString());

    final var downloader = new Downloader();
    final var currentFile = downloader.download(
        URI.create(params.getMavenRepo()).resolve(currentCoordinates.getFullPath()), currentCoordinates.getJarName());
    final var previousFile = downloader
        .download(URI.create(params.getMavenRepo()).resolve(previousCoordinates.getFullPath()),
                  previousCoordinates.getJarName());

    return CompletableFuture
        .allOf(currentFile, previousFile)
        .thenApply(ignored -> verifyDownloads(currentFile, previousFile))
        .join();
  }

  private ComparisonArtifacts getLocalFiles(final Args params) {
    final var previousJar = new File(params.getPreviousJar());
    final var currentJar = new File(params.getCurrentJar());
    if (!previousJar.exists()) {
      LOGGER.fail("File %s does not exist", previousJar);
    }
    if (!currentJar.exists()) {
      LOGGER.fail("File %s does not exist", currentJar);
    }
    return ImmutableComparisonArtifacts.of(currentJar, previousJar);
  }

  private boolean usingLocalFiles(final Args params) {
    return !Strings.isNullOrEmpty(params.getPreviousJar()) && !Strings.isNullOrEmpty(params.getCurrentJar());
  }

  private ComparisonArtifacts verifyDownloads(final CompletableFuture<File> currentFile,
                                              final CompletableFuture<File> previousFile) {
    final var current = Try.of(currentFile::get).onFailure(err -> LOGGER.fail(err.getMessage())).get();
    final var previous = Try.of(previousFile::get).onFailure(err -> LOGGER.fail(err.getMessage())).get();
    current.deleteOnExit();
    previous.deleteOnExit();
    return ImmutableComparisonArtifacts.of(current, previous);
  }

  private void compareApis(final ComparisonArtifacts artifacts) {
    LOGGER.print(ansi().a("Comparing public APIs of ")
                       .fgBrightBlue().a(artifacts.previousJar())
                       .fgDefault().a(" and ")
                       .fgBrightBlue().a(artifacts.currentJar())
                       .reset().toString());
    try {
      final ApiJar current = ApiJar.of(new JarFile(artifacts.currentJar()));
      final ApiJar previous = ApiJar.of(new JarFile(artifacts.currentJar()));

      previous.findDiff(current);
    } catch (final IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  private Coordinates paramsToSourceCoordinates(final Args params) {
    final var gradleParams = Optional.ofNullable(params.getCoordinates());
    final var groupId = Optional.ofNullable(params.getGroupId());
    final var artifactId = Optional.ofNullable(params.getArtifactId());
    final var version = Optional.ofNullable(params.getVersion());
    final var prevVersion = Optional.ofNullable(params.getPreviousVersion());

    if (prevVersion.isEmpty()) {
      throw new IllegalArgumentException("Previous version is required");
    }
    final var mavenParams =
        groupId.flatMap(gr -> artifactId.flatMap(art -> version.flatMap(v -> Optional.of(gr + ":" + art + ":" + v))));

    if (gradleParams.isPresent() && mavenParams.isPresent()) {
      throw new IllegalArgumentException(ansi().a("Please specify ")
                                               .fgBrightDefault().a("either")
                                               .fgDefault().a(" coordinates in gradle format ")
                                               .fgBrightDefault().a("or")
                                               .fgDefault().a(" use --groupId, --artifactId, --version options").reset()
                                               .toString());
    }
    final var coordinates = firstNonEmpty(gradleParams, mavenParams).map(Coordinates::fromGradle);
    if (coordinates.isEmpty()) {
      throw new IllegalArgumentException("Coordinates of the artifact are not provided");
    }
    else {
      return coordinates.get();
    }
  }

  @Value.Immutable
  interface ComparisonArtifacts {
    @Value.Parameter(order = 0)
    File currentJar();

    @Value.Parameter(order = 1)
    File previousJar();
  }
}

