package net.ninjacat.brking.diff.rules.fields;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class FieldAnnotationsChangedTest {
  @Test
  public void shouldFindAnnotationsDifference() {
    final var older = ApiClassParser.ofPublic(Older.class.getName());
    final var newer = ApiClassParser.of(Newer.class.getName());

    final var olderFields = FieldPool.of(older);
    final var newerFields = FieldPool.of(newer);

    final var differ = new FieldAnnotationsChanged();

    final var diff = differ.process(older, olderFields, newerFields);

    assertThat(diff, hasSize(1));
    assertThat(diff.get(0).diffType(), is(DiffType.FieldAnnotationsChanged));
    assertThat(diff.get(0).changedFrom(), is(Nonnull.class.getName()));
    assertThat(diff.get(0).changedTo(), is(Nullable.class.getName()));
  }

  public static class Older {
    @Nonnull
    public static String field = "";
  }

  public static class Newer {
    @Nullable
    public final String field = "a";
  }
}