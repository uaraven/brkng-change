package net.ninjacat.brking.diff.rules;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;

public class ClassRemovingInterfaces implements DiffRule<ApiClass> {
  @Override
  public Collection<DiffElement> process(final ApiClass older, final ApiClass newer) {
    final var olderInterfaces = Set.copyOf(older.interfaces());
    final var newerInterfaces = Set.copyOf(newer.interfaces());

    final var removed = olderInterfaces.stream()
                                       .filter(it -> !newerInterfaces.contains(it))
                                       .collect(Collectors.toUnmodifiableList());

    return removed.stream()
                  .map(intf -> ImmutableDiffElement
                      .builder()
                      .apiObject(older)
                      .description(
                          String.format("Interface '%s' has been removed", intf))
                      .severity(ChangeSeverity.BREAKING)
                      .build())
                  .collect(Collectors.toUnmodifiableSet());
  }
}

