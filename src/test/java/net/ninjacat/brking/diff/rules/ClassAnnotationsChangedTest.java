package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ClassAnnotationsChangedTest {
  @Test
  public void shouldFindChangedAnnotations() {
    final var older = ApiClassParser.of(Older.class.getName());
    final var newer = ApiClassParser.of(Newer.class.getName());

    final var differ = new ClassAnnotationsChanged();

    final var diff = differ.process(older, newer);

    assertThat(diff, hasSize(1));
    assertThat(diff.get(0).diffType(), is(DiffType.ClassAnnotationsChanged));
    assertThat(diff.get(0).changedFrom(), is(A1.class.getName()));
    assertThat(diff.get(0).changedTo(), is(A2.class.getName()));
  }

  @interface A1 {
  }

  @interface A2 {
  }

  @A1
  public static class Older {
  }

  @A2
  private static class Newer {
  }
}