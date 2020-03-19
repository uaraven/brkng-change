package net.ninjacat.brking.diff;

import net.ninjacat.brking.diff.ApiDiff.SortType;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.jar.JarFile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JarTest {
  @Test
  public void shouldDiffTwoJars() throws IOException {
    final var diff = ApiDiff.ofJars(
            new JarFile("testData/joda-convert-2.1.1.jar"),
            new JarFile("testData/joda-convert-2.1.2.jar"),
            SortType.BY_SEVERITY);

    assertThat(diff, Matchers.hasSize(36));
    assertThat(diff.stream().filter(it -> it.diffType().getSeverity() == ChangeSeverity.BREAKING).count(), is(0L));
  }
}
