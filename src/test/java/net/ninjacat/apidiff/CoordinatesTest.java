package net.ninjacat.apidiff;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CoordinatesTest {
  @Test
  public void shouldParseGradleCoordinates() {
    final var coordinates = ImmutableCoordinates.fromGradle("org.example:example:1.0");
    assertThat(coordinates.groupId(), is("org.example"));
    assertThat(coordinates.artifactId(), is("example"));
    assertThat(coordinates.version(), is("1.0"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailToCoordinatesWithPackaging() {
    ImmutableCoordinates.fromGradle("org.example:example:1.0:pom");
  }

  @Test
  public void shouldCreateJarName() {
    final var coordinates = ImmutableCoordinates.fromGradle("org.example:example:1.0");

    assertThat(coordinates.getJarName(), is("example-1.0.jar"));
  }

  @Test
  public void shouldCreateFullName() {
    final var coordinates = ImmutableCoordinates.fromGradle("org.example:example:1.0");

    assertThat(coordinates.getFullPath().toString(), is("/org/example/example/1.0/example-1.0.jar"));
  }
}