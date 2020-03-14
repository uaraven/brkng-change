package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.internal.ApiClassParser;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;


public class ClassVisibilityChangedTest {

    @Test
    public void shouldFindDifference() {
        final var older = ApiClassParser.of(Older.class.getName());
        final var newer = ApiClassParser.of(Newer.class.getName());

        final var differ = new ClassVisibilityChanged();

        final var diff = differ.process(older, newer);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).description(), containsString("Visibility"));
    }

    public static class Older {
    }

    ;

    private static class Newer {
    }

    ;
}