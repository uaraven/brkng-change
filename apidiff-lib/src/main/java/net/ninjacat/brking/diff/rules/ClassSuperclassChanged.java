package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;

public class ClassSuperclassChanged implements ClassDiffRule {
    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer, final DiffContext context) {

        final var diffType = context.options().relaxedInheritance()
                && context.newerPool().forClass(newer.superName())
                .map(it -> it.isSubclassOf(older.superName())).orElse(false)
                ? DiffType.ClassSuperClassChangedToSubclass
                : DiffType.ClassSuperClassChanged;

        if (!older.superName().equals(newer.superName())) {
            return List.of(
                    ImmutableDiffElement.builder()
                            .ownerClass(older)
                            .changedObject(older)
                            .changedFrom(older.superName())
                            .changedTo(newer.superName())
                            .diffType(diffType)
                            .build()
            );
        } else {
            return List.of();
        }
    }
}
