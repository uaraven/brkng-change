package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ClassInterfacesAddedTest {

    @Test
    public void shouldFindAddedInterface() {
        final var older = ApiClassParser.of(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var differ = new ClassInterfacesAdded();

        final var diff = differ.process(older, newer, null);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.ClassInterfaceAdded));
        assertThat(diff.get(0).changedTo(), containsString(Someinterface.class.getName()));
    }

    interface Someinterface {
    }

    public static class Older {
    }

    private static class Newer implements Someinterface {
    }
}