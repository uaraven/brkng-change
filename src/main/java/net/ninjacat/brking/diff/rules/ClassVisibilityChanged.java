package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;
import net.ninjacat.brking.utils.AsmUtils;

import java.util.List;

import static net.ninjacat.brking.utils.AsmUtils.visibilityToString;

public class ClassVisibilityChanged implements ClassDiffRule {
    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer) {
        final var stricterAccess = AsmUtils.hasAccessChangedToStricter(older.access(), newer.access());
        return stricterAccess
                ? List.of(ImmutableDiffElement.builder()
                .ownerClass(older)
                .diffType(DiffType.ClassVisibilityChanged)
                .changedObject(older)
                .changedFrom(visibilityToString(older.access()))
                .changedTo(visibilityToString(newer.access()))
                .description("Visibility changed from '${changed.from}' to '${changed.to}'")
                .build())
                : List.of();
    }
}
