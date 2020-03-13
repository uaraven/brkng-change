package net.ninjacat.brking.diff.rules;

import java.util.Collection;
import java.util.stream.Collectors;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;
import net.ninjacat.brking.utils.AsmUtils;

public class ClassAccess implements DiffRule<ApiClass> {
  @Override
  public Collection<DiffElement> process(final ApiClass older, final ApiClass newer) {
    final var stricterAccessWarning = AsmUtils.stricterAccessWarning(newer.access(), older.access());
    return stricterAccessWarning.map(
        warning ->
            ImmutableDiffElement.builder()
                                .apiObject(older)
                                .severity(ChangeSeverity.BREAKING)
                                .description(warning)
                                .build()).stream()
                                .collect(Collectors.toUnmodifiableList());
  }
}
