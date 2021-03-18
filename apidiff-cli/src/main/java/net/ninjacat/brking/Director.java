package net.ninjacat.brking;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.jar.JarFile;

import io.vavr.control.Try;
import net.ninjacat.brking.diff.ApiDiff;
import net.ninjacat.brking.diff.ApiDiff.SortType;
import net.ninjacat.brking.diff.ImmutableDiffOptions;
import net.ninjacat.brking.logging.ConsoleLogger;
import net.ninjacat.brking.logging.Logger;
import net.ninjacat.brking.net.Downloader;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.immutables.value.Value;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.is;
import static org.fusesource.jansi.Ansi.ansi;

public class Director
{
  private static final Logger LOGGER = ConsoleLogger.getLogger();

  public void execute(final Args params) {
    final ComparisonArtifacts artifacts = getArtifacts(params);
    compareApis(params, artifacts);
  }

  private ComparisonArtifacts getArtifacts(final Args params) {
    final CompletableFuture<File> currentFile = getJar(params, params.getCurrentJar());
    final CompletableFuture<File> previousFile = getJar(params, params.getPreviousJar());

    return CompletableFuture
        .allOf(currentFile, previousFile)
        .thenApply(ignored -> verifyDownloads(currentFile, previousFile))
        .join();
  }

  private CompletableFuture<File> getJar(final Args args, final String jarPointer) {
    final var sourceType = getSourceType(jarPointer);
    return Match(sourceType).of(
        Case($(is(SourceType.FILE)), f -> getFile(jarPointer)),
        Case($(is(SourceType.MAVEN)), f -> getMavenArtifact(args, jarPointer)),
        Case($(is(SourceType.URL)), f -> getFileFromUrl(jarPointer))
    );
  }

  private SourceType getSourceType(final String jarPointer) {
    if (jarPointer == null) {
      throw new IllegalArgumentException("Jar not provided");
    }
    if (Files.exists(Paths.get(jarPointer))) {
      return SourceType.FILE;
    }
    else if (isMaven(jarPointer)) {
      return SourceType.MAVEN;
    }
    else if (Try.of(() -> URI.create(jarPointer)).isSuccess()) {
      return SourceType.URL;
    }
    else {
      throw new IllegalArgumentException("Invalid source: " + jarPointer);
    }
  }

  private CompletableFuture<File> getFileFromUrl(final String url) {
    final var jarName = Try.of(() -> Files.createTempFile("diff", ".jar"))
        .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
    jarName.toFile().deleteOnExit();

    LOGGER.print(ansi().a("Downloading from ").fgBrightBlue().a(url).reset().toString());
    final var downloader = new Downloader();
    return downloader.download(URI.create(url),
        null,
        jarName.toString());
  }

  private CompletableFuture<File> getFile(final String path) {
    LOGGER.print(ansi().a("Using file ")
        .fgBrightBlue().a(path).reset().toString());

    return CompletableFuture.completedFuture(new File(path));
  }

  private CompletableFuture<File> getMavenArtifact(final Args args, final String coordinates) {
    final var coords = Coordinates.parse(coordinates);
    LOGGER.print(ansi().a("Retrieving ")
        .fgBrightBlue().a(coords.getJarName())
        .fgDefault().a(" from Maven").reset().toString());
    final var downloader = new Downloader();
    return downloader.download(
        URI.create(args.getMavenRepo()).resolve(coords.getFullPath()),
        args.getRepoAuth(),
        coords.getJarName());
  }

  private boolean isMaven(final String coords) {
    return Try.of(() -> new DefaultArtifact(coords)).isSuccess();
  }

  private ComparisonArtifacts verifyDownloads(
      final CompletableFuture<File> currentFile,
      final CompletableFuture<File> previousFile)
  {
    final var current = Try.of(currentFile::get);
    final var previous = Try.of(previousFile::get);

    return current.flatMap(cur -> previous.map(prev -> ImmutableComparisonArtifacts.of(cur, prev)))
        .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
  }

  private void compareApis(final Args params, final ComparisonArtifacts artifacts) {
    LOGGER.print(ansi().a("Comparing public APIs of ")
        .fgBrightBlue().a(artifacts.previousJar().toPath().getFileName())
        .fgDefault().a(" and ")
        .fgBrightBlue().a(artifacts.currentJar().toPath().getFileName())
        .reset().toString());
    try {
      final var options = ImmutableDiffOptions.builder()
          .relaxedInheritance(params.isRelaxed())
          .isPublicOnly(params.isPublicOnly())
          .build();

      final var diff = ApiDiff.ofJars(
          new JarFile(artifacts.previousJar()),
          new JarFile(artifacts.currentJar()),
          SortType.BY_SEVERITY,
          options);

      final var printer = params.getOutputFormat().getPrinter(params);
      final PrintStream output;
      if (params.getOutputPath() != null) {
        output = new PrintStream(new FileOutputStream(params.getOutputPath()));
      }
      else {
        output = System.out;
      }
      printer.print(output, diff);
      output.flush();
    }
    catch (final IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  @Value.Immutable
  interface ComparisonArtifacts
  {
    @Value.Parameter(order = 0)
    File currentJar();

    @Value.Parameter(order = 1)
    File previousJar();
  }

  private enum SourceType
  {
    MAVEN,
    FILE,
    URL
  }
}

