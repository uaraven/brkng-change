package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.internal.ApiClassParser;
import net.ninjacat.brking.diff.DiffType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FieldRemovedTest {

    @Test
    public void shouldFindTypeDifference() {
        final var older = ApiClassParser.ofPublic(Older.class.getName()).get();
        final var newer = ApiClassParser.of(Newer.class.getName()).get();

        final var olderFields = FieldPool.of(older);
        final var newerFields = FieldPool.of(newer);

        final var differ = new FieldRemoved();

        final var diff = differ.process(older, olderFields, newerFields);

        assertThat(diff, hasSize(1));
        assertThat(diff.get(0).diffType(), is(DiffType.FieldRemoved));
        assertThat(diff.get(0).changedFrom(), containsString("public java.lang.String field"));
    }


    public static class Older {
        public String field;
    }

    public static class Newer {
        public String field2;
    }
}