package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class FieldDeprecatedTest
{
  @Test
  public void shouldFindDeprecation() {
    final var older = ApiClassParser.ofPublic(Older.class.getName()).get();
    final var newer = ApiClassParser.of(Newer.class.getName()).get();

    final var olderFields = FieldPool.of(older);
    final var newerFields = FieldPool.of(newer);

    final var differ = new FieldDeprecated();

    final var diff = differ.process(older, olderFields, newerFields, null);

    assertThat(diff, hasSize(1));
    assertThat(diff.get(0).diffType(), is(DiffType.FieldDeprecated));
  }

  public static class Older
  {
    public static String field = "";
  }

  public static class Newer
  {
    @Deprecated
    public final String field = "a";
  }
}