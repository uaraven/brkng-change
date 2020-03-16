package net.ninjacat.brking.diff.rules;

import java.util.List;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

public class ClassSuperclassChanged implements ClassDiffRule {
    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer) {
        if (!older.superName().equals(newer.superName())) {
            return List.of(
                    ImmutableDiffElement.builder()
                            .ownerClass(older)
                            .changedObject(older)
                            .changedFrom(older.superName())
                            .changedTo(newer.superName())
                            .diffType(DiffType.ClassSuperClassChanged)
                            .build()
            );
        } else {
      return List.of();
    }
  }
}
