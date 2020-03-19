package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class MethodAnnotationsChangedTest {
  @Test
  public void shouldFindAnnotationsDifference() {
    final var older = ApiClassParser.ofPublic(Older.class.getName()).get();
    final var newer = ApiClassParser.of(Newer.class.getName()).get();

    final var olderFields = MethodPool.of(older);
    final var newerFields = MethodPool.of(newer);

    final var differ = new MethodAnnotationsChanged();

    final var diff = differ.process(older, olderFields, newerFields, null);

    assertThat(diff, hasSize(1));
    assertThat(diff.get(0).changedFrom(), is(Nonnull.class.getName()));
    assertThat(diff.get(0).changedTo(), is(Nullable.class.getName()));
    assertThat(diff.get(0).diffType(), is(DiffType.MethodAnnotationsChanged));
  }

  public static class Older {
    @Nonnull
    public Integer callMe() {
      return 0;
    }
  }

  public static class Newer {
    @Nullable
    public Integer callMe() {
      return 0;
    }
  }
}