package net.ninjacat.brking.diff.rules;

import java.util.List;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import static net.ninjacat.brking.utils.AnnotationUtils.hasDeprecated;

public class ClassDeprecated
    implements ClassDiffRule
{
    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer, final DiffContext context) {
        if (!hasDeprecated(older) && hasDeprecated(newer)) {
            return List.of(ImmutableDiffElement.builder()
                .ownerClass(older)
                .changedObject(older)
                .diffType(DiffType.ClassDeprecated)
                .changedTo("Deprecated")
                .build());
        }
        else {
            return List.of();
        }
    }
}

