package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassImplementingNewInterfaces implements ClassDiffRule {
  @Override
  public List<DiffElement> process(final ApiClass older, final ApiClass newer) {
    final var olderInterfaces = Set.copyOf(older.interfaces());
    final var newerInterfaces = Set.copyOf(newer.interfaces());

    final var removed = newerInterfaces.stream()
            .filter(it -> !olderInterfaces.contains(it))
            .collect(Collectors.toUnmodifiableList());

    return removed.stream()
            .map(intf -> ImmutableDiffElement
                    .builder()
                    .apiObject(older)
                    .description(
                            String.format("Interface '%s' has been added", intf))
                    .severity(ChangeSeverity.SAFE)
                    .build())
            .collect(Collectors.toUnmodifiableList());
  }
}

