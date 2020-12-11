package net.ninjacat.brking.diff;

import java.io.IOException;
import java.util.jar.JarFile;

import net.ninjacat.brking.diff.ApiDiff.SortType;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JarTest {
  @Test
  public void shouldDiffTwoJars() throws IOException {
    final var diff = ApiDiff.ofJars(
        new JarFile("../testData/joda-convert-1.9.2.jar"),
        new JarFile("../testData/joda-convert-2.1.2.jar"),
        SortType.BY_SEVERITY);

    assertThat(diff, Matchers.hasSize(1));
    assertThat(diff.stream().filter(it -> it.diffType().getSeverity() == ChangeSeverity.BREAKING).count(), is(0L));
  }

  @Test
  public void shouldDiffTwoJarsNonPublic() throws IOException {
    final var diff = ApiDiff.ofJars(
        new JarFile("../testData/joda-convert-1.9.2.jar"),
        new JarFile("../testData/joda-convert-2.1.2.jar"),
        SortType.BY_SEVERITY,
        ImmutableDiffOptions.builder().isPublicOnly(false).build()
    );

    assertThat(diff, Matchers.hasSize(23));
    assertThat(diff.stream().filter(it -> it.diffType().getSeverity() == ChangeSeverity.BREAKING).count(), is(14L));
  }

  @Test
  public void shouldFindClassIncrement() throws IOException {
    final var diff = ApiDiff.ofJars(
        new JarFile("../testData/test-j8.jar"),
        new JarFile("../testData/test-j11.jar"),
        SortType.BY_SEVERITY);

    assertThat(diff, Matchers.hasSize(1));
    assertThat(diff.get(0).diffType(), is(DiffType.ClassVersionIncremented));
  }

  @Test
  public void shouldFindClassDecrement() throws IOException {
    final var diff = ApiDiff.ofJars(
            new JarFile("../testData/test-j11.jar"),
            new JarFile("../testData/test-j8.jar"),
            SortType.BY_SEVERITY);

    assertThat(diff, Matchers.hasSize(1));
    assertThat(diff.get(0).diffType(), is(DiffType.ClassVersionDecremented));
  }
}
