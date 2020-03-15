package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;
import net.ninjacat.brking.utils.AsmUtils;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassModifiersChanged implements ClassDiffRule {
    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer) {
        final List<String> modifiers = new ArrayList<>();
        checkModifierChange(older, newer, modifiers, Opcodes.ACC_STATIC, "static");
        checkModifierChange(older, newer, modifiers, Opcodes.ACC_ABSTRACT, "abstract");
        checkModifierChange(older, newer, modifiers, Opcodes.ACC_FINAL, "final");
        if (!modifiers.isEmpty()) {
            return List.of(ImmutableDiffElement.builder()
                    .ownerClass(older)
                    .severity(ChangeSeverity.BREAKING)
                    .changedObject(older)
                    .changedTo(String.join(",", modifiers))
                    .description("Class modifiers has changed to ${changed.to}")
                    .build()).stream()
                    .collect(Collectors.toUnmodifiableList());
        } else {
            return List.of();
        }
    }

    private void checkModifierChange(final ApiClass older,
                                     final ApiClass newer,
                                     final List<String> fails,
                                     final int modBit,
                                     final String modifier) {
        if (AsmUtils.hasModifierChanged(older, newer, modBit)) {
            fails.add(modifier);
        }
    }

}
