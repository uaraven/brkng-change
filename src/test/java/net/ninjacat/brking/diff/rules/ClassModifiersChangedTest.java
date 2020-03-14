package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.rules.p1.Older1;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

public class ClassModifiersChangedTest {

    @Test
    public void shouldFindDifferenceInFinal() {
        final var older = ApiClassParser.of(Older.class.getName());
        final var newer = ApiClassParser.of(Newer.class.getName());

        final var differ = new ClassModifiersChanged();

        final var diff = differ.process(older, newer);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).description(), containsString("final"));
    }

    @Test
    public void shouldFindDifferenceInAbstract() {
        final var older = ApiClassParser.of(Older1.class.getName());
        final var newer = ApiClassParser.of(net.ninjacat.brking.diff.rules.p2.Older1.class.getName());

        final var differ = new ClassModifiersChanged();

        final var diff = differ.process(older, newer);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).description(), containsString("abstract"));
    }


    public static class Older {
    }

    public static final class Newer {
    }
}