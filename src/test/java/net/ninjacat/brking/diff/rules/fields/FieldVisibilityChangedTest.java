package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class FieldVisibilityChangedTest {

    @Test
    public void shouldFindModifiersDifference() {
        final var older = ApiClassParser.ofPublic(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var olderFields = FieldPool.of(older);
        final var newerFields = FieldPool.of(newer);

        final var differ = new FieldVisibilityChanged();

        final var diff = differ.process(older, olderFields, newerFields, null);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.FieldVisibilityChanged));
        assertThat(diff.get(0).changedTo(), is("package private"));
        assertThat(diff.get(0).changedFrom(), is("public"));
    }


    public static class Older {
        public static String field;
    }

    public static class Newer {
        static String field = "a";
    }
}