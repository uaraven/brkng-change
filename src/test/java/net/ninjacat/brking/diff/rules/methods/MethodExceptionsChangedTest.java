package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.internal.ApiClassParser;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesRegex;

public class MethodExceptionsChangedTest {
    @Test
    public void shouldFindModifiersDifference() {
        final var older = ApiClassParser.ofPublic(Older.class.getName());
        final var newer = ApiClassParser.of(Newer.class.getName());

        final var olderFields = MethodPool.of(older);
        final var newerFields = MethodPool.of(newer);

        final var differ = new MethodExceptionsChanged();

        final var diff = differ.process(older, olderFields, newerFields);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).description(), matchesRegex(".*'public int callMe\\(\\)'.*changed to 'java.lang.IllegalArgumentException,java.lang.NumberFormatException'"));
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