package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;

public class ClassAnnotationsChanged implements ClassDiffRule {
    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer, DiffContext context) {
        if (!older.annotations().equals(newer.annotations())) {
            return List.of(ImmutableDiffElement.builder()
                    .ownerClass(older)
                    .changedObject(older)
                    .diffType(DiffType.ClassAnnotationsChanged)
                    .changedFrom(older.annotationNames())
                    .changedTo(newer.annotationNames())
                    .build());
        } else {
            return List.of();
    }
  }
}

