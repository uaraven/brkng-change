package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class MethodModifiersChangedTest {
    @Test
    public void shouldFindModifiersDifference() {
        final var older = ApiClassParser.ofPublic(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var olderFields = MethodPool.of(older);
        final var newerFields = MethodPool.of(newer);

        final var differ = new MethodModifiersChanged();

        final var diff = differ.process(older, olderFields, newerFields, null);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.MethodModifiersChanged));
        assertThat(diff.get(0).changedTo(), is("strictfp public"));
        assertThat(diff.get(0).changedFrom(), is("public"));
    }


    public static class Older {
        public int callMe() {
            return 0;
        }
    }

    public static class Newer {
        public strictfp int callMe() {
            return 0;
        }
    }
}