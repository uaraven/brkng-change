package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.ChangeSeverity;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ClassInterfacesAddedTest {

    @Test
    public void shouldFindAddedInterface() {
        final var older = ApiClassParser.of(Older.class.getName());
        final var newer = ApiClassParser.of(Newer.class.getName());

        final var differ = new ClassInterfacesAdded();

        final var diff = differ.process(older, newer);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).severity(), is(ChangeSeverity.SAFE));
        assertThat(diff.get(0).description(), containsString(Someinterface.class.getName()));
        assertThat(diff.get(0).description(), matchesRegex("Interface.*has been added"));
    }

    interface Someinterface {
    }

    public static class Older {
    }

    private static class Newer implements Someinterface {
    }
}