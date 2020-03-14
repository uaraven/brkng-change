package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.internal.ApiClassParser;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;


public class ClassSuperClassChangedTest {

    @Test
    public void shouldFindVisibilityDifference() {
        final var older = ApiClassParser.of(Older.class.getName());
        final var newer = ApiClassParser.of(Newer.class.getName());

        final var differ = new ClassSuperclassChanged();

        final var diff = differ.process(older, newer);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).description(), containsString("Super class changed"));
        assertThat(diff.get(0).description(), containsString(Parent.class.getName()));
    }

    public static class Older {
    }

    public static class Parent {

    }

    private static class Newer extends Parent {
    }
}