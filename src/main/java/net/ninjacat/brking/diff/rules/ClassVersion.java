package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;

public class ClassVersion implements ClassDiffRule {
    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer) {
        final var builder = ImmutableDiffElement.builder().ownerClass(older).changedObject(older);

        switch (Integer.compare(older.version(), newer.version())) {
            case -1:
                builder.severity(ChangeSeverity.WARNING)
                        .changedFrom(String.valueOf(older.version()))
                        .changedTo(String.valueOf(newer.version()))
                        .description("Bytecode version has changed from ${changed.from} to ${changed.to}");
                break;
            case 0:
                return List.of();
            case 1:
                builder.severity(ChangeSeverity.SAFE)
                        .description("Bytecode version has changed from ${changed.from} to ${changed.to}");
                break;
        }
        return List.of(builder.build());
    }
}
