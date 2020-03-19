package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


public class ClassInterfacesRemovedTest {

    @Test
    public void shouldFindRemovedInterface() {
        final var older = ApiClassParser.of(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var differ = new ClassInterfacesRemoved();

        final var diff = differ.process(older, newer);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.ClassInterfaceRemoved));
        assertThat(diff.get(0).changedFrom(), is(Someinterface.class.getName()));
    }

    interface Someinterface {
    }

    public static class Older implements Someinterface {
    }

    private static class Newer {
    }
}