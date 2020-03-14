package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.internal.ApiClassParser;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ClassInterfacesRemovedTest {

    @Test
    public void shouldFindVisibilityDifference() {
        final var older = ApiClassParser.of(Older.class.getName());
        final var newer = ApiClassParser.of(Newer.class.getName());

        final var differ = new ClassInterfacesRemoved();

        final var diff = differ.process(older, newer);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).description(), containsString(Someinterface.class.getName()));
        assertThat(diff.get(0).description(), matchesRegex("Interface.*has been removed"));
    }

    interface Someinterface {
    }

    public static class Older implements Someinterface {
    }

    private static class Newer {
    }
}