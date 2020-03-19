package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ClassPool;
import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.ImmutableDiffContext;
import net.ninjacat.brking.diff.ImmutableDiffOptions;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ClassSuperClassChangedTest {
    @Test
    public void shouldFindChangedSuperclass() {
        final var older = ApiClassParser.of(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var differ = new ClassSuperclassChanged();

        final var diff = differ.process(older, newer, DiffContext.empty());

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).changedFrom(), is(OlderParent.class.getName()));
        assertThat(diff.get(0).changedTo(), is(NewerParent.class.getName()));
    }

    @Test
    public void shouldFindChangedSuperclassRelaxed() {
        final var older = ApiClassParser.of(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var type1 = ApiClassParser.of(OlderParent.class.getName()).get();
        final var type2 = ApiClassParser.of(NewerParent.class.getName()).get();

        final var olderPool = new ClassPool(List.of(older));
        final var newerPool = new ClassPool(List.of(older, type1, type2));
        final var context = ImmutableDiffContext.builder().olderPool(olderPool).newerPool(newerPool)
                .options(ImmutableDiffOptions.builder().relaxedInheritance(true).build()).build();


        final var differ = new ClassSuperclassChanged();

        final var diff = differ.process(older, newer, context);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).changedFrom(), is(OlderParent.class.getName()));
        assertThat(diff.get(0).changedTo(), is(NewerParent.class.getName()));
    }

    @Test
    public void shouldFindChangedSuperclassRelaxedNoInheritance() {
        final var older = ApiClassParser.of(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var type2 = ApiClassParser.of(NewerParent.class.getName()).get();

        final var olderPool = new ClassPool(List.of(older));
        final var newerPool = new ClassPool(List.of(older, type2));
        final var context = ImmutableDiffContext.builder().olderPool(olderPool).newerPool(newerPool)
                .options(ImmutableDiffOptions.builder().relaxedInheritance(true).build()).build();


        final var differ = new ClassSuperclassChanged();

        final var diff = differ.process(older, newer, context);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).changedFrom(), is(OlderParent.class.getName()));
        assertThat(diff.get(0).changedTo(), is(NewerParent.class.getName()));
    }

    public static class OlderParent {
    }


    public static class Older extends OlderParent {
    }

    public static class NewerParent extends OlderParent {
    }

    private static class Newer extends NewerParent {
    }
}