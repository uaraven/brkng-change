package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassInterfacesAdded implements ClassDiffRule {
    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer, DiffContext context) {
        final var olderInterfaces = Set.copyOf(older.interfaces());
        final var newerInterfaces = Set.copyOf(newer.interfaces());

        final var removed = newerInterfaces.stream()
                .filter(it -> !olderInterfaces.contains(it))
                .collect(Collectors.toUnmodifiableList());

        return removed.stream()
                .map(intf -> ImmutableDiffElement
                        .builder()
                    .ownerClass(older)
                    .changedObject(older)
                    .changedTo(intf)
                    .diffType(DiffType.ClassInterfaceAdded)
                    .build())
            .collect(Collectors.toUnmodifiableList());
  }
}

