package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.internal.ApiClassParser;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesRegex;

public class FieldVisibilityChangedTest {

    @Test
    public void shouldFindModifiersDifference() {
        final var older = ApiClassParser.ofPublic(Older.class.getName());
        final var newer = ApiClassParser.of(Newer.class.getName());

        final var olderFields = FieldPool.of(older);
        final var newerFields = FieldPool.of(newer);

        final var differ = new FieldVisibilityChanged();

        final var diff = differ.process(older, olderFields, newerFields);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).description(), matchesRegex(".*'public static java.lang.String field'.*changed to 'package private'"));
    }


    public static class Older {
        public static String field;
    }

    public static class Newer {
        static String field = "a";
    }
}