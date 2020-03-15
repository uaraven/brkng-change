package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;

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
                            .severity(ChangeSeverity.BREAKING)
                            .description(String.format("Super class changed from '%s' to '%s'", older.superName(),
                                    newer.superName()))
                            .build()
            );
        } else {
      return List.of();
    }
  }
}
