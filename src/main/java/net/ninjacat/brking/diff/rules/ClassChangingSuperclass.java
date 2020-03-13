package net.ninjacat.brking.diff.rules;

import java.util.Collection;
import java.util.List;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;

public class ClassChangingSuperclass implements DiffRule<ApiClass> {
  @Override
  public Collection<DiffElement> process(final ApiClass older, final ApiClass newer) {
    if (!older.superName().equals(newer.superName())) {
      return List.of(
          ImmutableDiffElement.builder()
                              .apiObject(older)
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
