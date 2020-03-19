package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class MethodExceptionsChangedTest {
    @Test
    public void shouldFindModifiersDifference() {
        final var older = ApiClassParser.ofPublic(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var olderFields = MethodPool.of(older);
        final var newerFields = MethodPool.of(newer);

        final var differ = new MethodExceptionsChanged();

        final var diff = differ.process(older, olderFields, newerFields, null);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).changedTo(), is("java.lang.IllegalArgumentException,java.lang.NumberFormatException"));
        assertThat(diff.get(0).diffType(), is(DiffType.MethodExceptionsChanged));
    }


    public static class Older {
        public int callMe() throws IOException {
            return 0;
        }
    }

    public static class Newer {
        public int callMe() throws IllegalArgumentException, NumberFormatException {
            return 0;
        }
    }
}