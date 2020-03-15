package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.internal.ApiClassParser;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesRegex;

public class MethodRemovedTest {
    @Test
    public void shouldFindRemovedMethod() {
        final var older = ApiClassParser.ofPublic(Older.class.getName());
        final var newer = ApiClassParser.of(Newer.class.getName());

        final var olderFields = MethodPool.of(older);
        final var newerFields = MethodPool.of(newer);

        final var differ = new MethodRemoved();

        final var diff = differ.process(older, olderFields, newerFields);

        assertThat(diff, hasSize(1));
        MatcherAssert.assertThat(diff.get(0).description(), matchesRegex(".*'public int callMe\\(\\)'.*was removed"));
    }


    public static class Older {
        public int callMe() {
            return 0;
        }
    }

    public static class Newer {
        public int callMe(int param) {
            return 0;
        }
    }
}