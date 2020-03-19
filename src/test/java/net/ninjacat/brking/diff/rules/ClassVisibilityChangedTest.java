package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.internal.ApiClassParser;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ClassVisibilityChangedTest {

    @Test
    public void shouldFindVisibilityDifference() {
        final var older = ApiClassParser.of(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var differ = new ClassVisibilityChanged();

        final var diff = differ.process(older, newer);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).changedFrom(), is("public"));
        assertThat(diff.get(0).changedTo(), is("package private"));
    }

    public static class Older {
    }

    private static class Newer {
    }
}