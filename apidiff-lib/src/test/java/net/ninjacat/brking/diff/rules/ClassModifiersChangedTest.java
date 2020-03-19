package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.rules.p1.Older1;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ClassModifiersChangedTest {

    @Test
    public void shouldFindDifferenceInFinal() {
        final var older = ApiClassParser.of(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var differ = new ClassModifiersChanged();

        final var diff = differ.process(older, newer, null);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.ClassModifiersChanged));
        assertThat(diff.get(0).changedTo(), containsString("final"));
    }

    @Test
    public void shouldFindDifferenceInAbstract() {
        final var older = ApiClassParser.of(Older1.class.getName()).get();
        final var newer = ApiClassParser.of(net.ninjacat.brking.diff.rules.p2.Older1.class.getName()).get();

        final var differ = new ClassModifiersChanged();

        final var diff = differ.process(older, newer, null);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.ClassModifiersChanged));
        assertThat(diff.get(0).changedTo(), containsString("abstract"));
    }


    public static class Older {
    }

    public static final class Newer {
    }
}