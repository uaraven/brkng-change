package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class MutilpleInheritedMethodTest {
    @Test
    public void shouldFindRemovedMethod() {
        final var older = ApiClassParser.ofPublic(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var olderFields = MethodPool.of(older);
        final var newerFields = MethodPool.of(newer);

        final var differ = new MethodAdded();

        final var diff = differ.process(older, olderFields, newerFields);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.MethodAdded));
        assertThat(diff.get(0).changedFrom(), is(""));
        assertThat(diff.get(0).changedTo(), is("public int callMe(int)"));
    }

    public static class Some {
    }

    ;

    public interface Parent {
        Some callSome(int i);

        double callSome(double i);
    }

    public static class Older implements Parent {
        public int callMe() {
            return 0;
        }

        @Override
        public Some callSome(final int i) {
            return new Some();
        }

        @Override
        public double callSome(final double i) {
            return 0;
        }
    }

    public static class Newer implements Parent {
        public int callMe(int param) {
            return 0;
        }

        @Override
        public Some callSome(final int i) {
            return new Some();
        }

        @Override
        public double callSome(final double i) {
            return 0;
        }
    }
}