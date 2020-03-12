package net.ninjacat.apidiff;

import java.nio.file.Paths;

import io.vavr.collection.List;
import org.immutables.value.Value;

@Value.Immutable
public abstract class Coordinates {
  @Value.Parameter(order = 0)
  public abstract String groupId();

  @Value.Parameter(order = 1)
  public abstract String artifactId();

  @Value.Parameter(order = 2)
  public abstract String version();

  public SemVer versionAsSemVer() {
    return SemVer.of(version());
  }

  public String getJarName() {
    return artifactId() + "-" + version() + ".jar";
  }

  public String getFullPath() {
    return Paths.get("/" + groupId().replace(".", "/")).resolve(artifactId()).resolve(version()).resolve(getJarName())
                .toString();
  }

  public static Coordinates fromGradle(final String gradleCoords) {
    final var parts = List.of(gradleCoords.split(":")).padTo(3, "");
    if (parts.length() > 3) {
      throw new IllegalArgumentException("Packaging is not supported for coordinates, JAR is always assumed");
    }
    return ImmutableCoordinates.of(parts.get(0), parts.get(1), parts.get(2));
  }
}
