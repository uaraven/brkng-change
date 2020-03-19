package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.ClassPool;
import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffContext;
import net.ninjacat.brking.diff.ImmutableDiffOptions;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class MethodReturnTypeChangedTest {
    @Test
    public void shouldFindModifiersDifference() {
        final var older = ApiClassParser.ofPublic(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var olderFields = MethodPool.of(older);
        final var newerFields = MethodPool.of(newer);

        final var differ = new MethodReturnTypeChanged();

        final var diff = differ.process(older, olderFields, newerFields, DiffContext.empty());

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.MethodReturnTypeChanged));
        assertThat(diff.get(0).changedTo(), is(Type2.class.getName()));
        assertThat(diff.get(0).changedFrom(), is(Type1.class.getName()));
    }

    @Test
    public void shouldFindModifiersDifferenceRelaxed() {
        final var older = ApiClassParser.ofPublic(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();


        final var type1 = ApiClassParser.of(Type1.class.getName()).get();
        final var type2 = ApiClassParser.of(Type2.class.getName()).get();

        final var olderPool = new ClassPool(List.of(older));
        final var newerPool = new ClassPool(List.of(older, type1, type2));
        final var context = ImmutableDiffContext.builder().olderPool(olderPool).newerPool(newerPool)
                .options(ImmutableDiffOptions.builder().relaxedInheritance(true).build()).build();

        final var olderFields = MethodPool.of(older);
        final var newerFields = MethodPool.of(newer);

        final var differ = new MethodReturnTypeChanged();

        final var diff = differ.process(older, olderFields, newerFields, context);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.MethodReturnTypeChangedToSubclass));
        assertThat(diff.get(0).changedTo(), is(Type2.class.getName()));
        assertThat(diff.get(0).changedFrom(), is(Type1.class.getName()));

    }

    @Test
    public void shouldFindModifiersDifferenceRelaxedNoInheritance() {
        final var older = ApiClassParser.ofPublic(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();


        final var type2 = ApiClassParser.of(Type2.class.getName()).get();

        final var olderPool = new ClassPool(List.of(older));
        final var newerPool = new ClassPool(List.of(older, type2));
        final var context = ImmutableDiffContext.builder().olderPool(olderPool).newerPool(newerPool)
                .options(ImmutableDiffOptions.builder().relaxedInheritance(true).build()).build();

        final var olderFields = MethodPool.of(older);
        final var newerFields = MethodPool.of(newer);

        final var differ = new MethodReturnTypeChanged();

        final var diff = differ.process(older, olderFields, newerFields, context);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.MethodReturnTypeChanged));
        assertThat(diff.get(0).changedTo(), is(Type2.class.getName()));
        assertThat(diff.get(0).changedFrom(), is(Type1.class.getName()));

    }

    public static class Type1 {
    }

    public static class Type2 extends Type1 {
    }

    ;

    public static class Older {
        public Type1 callMe() {
            return null;
        }
    }

    public static class Newer {
        public Type2 callMe() {
            return null;
        }
    }
}