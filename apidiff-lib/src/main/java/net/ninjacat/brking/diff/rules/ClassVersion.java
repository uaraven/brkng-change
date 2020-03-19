package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;

public class ClassVersion implements ClassDiffRule {
    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer, DiffContext context) {
        final var builder = ImmutableDiffElement.builder().ownerClass(older).changedObject(older);

        switch (Integer.compare(older.version(), newer.version())) {
            case -1:
                builder.diffType(DiffType.ClassVersionIncremented)
                        .changedFrom(String.valueOf(older.version()))
                        .changedTo(String.valueOf(newer.version()));
                break;
            case 0:
                return List.of();
            case 1:
                builder.diffType(DiffType.ClassVersionDecremented)
                       .changedFrom(String.valueOf(older.version()))
                       .changedTo(String.valueOf(newer.version()));
                break;
        }
        return List.of(builder.build());
    }
}
