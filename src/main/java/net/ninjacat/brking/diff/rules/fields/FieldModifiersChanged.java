package net.ninjacat.brking.diff.rules.fields;

import io.vavr.Tuple;
import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;
import net.ninjacat.brking.utils.AsmUtils;

import java.util.List;
import java.util.stream.Collectors;

public class FieldModifiersChanged implements FieldDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiField> older,
                                     final ApiObjectPool<ApiField> newer) {
        final var matching = matchingOnly(older, newer);
        return matching.stream()
                .map(field -> Tuple.of(older.get(field.name()), newer.get(field.name())))
                .filter(pair -> AsmUtils.hasModifierChanged(pair._1(), pair._2()))
                .map(pair -> ImmutableDiffElement.builder()
                        .ownerClass(reference)
                        .severity(ChangeSeverity.BREAKING)
                        .description(String.format("Field '%s' modifiers changed to '%s'",
                                pair._1().apiName(),
                                AsmUtils.visibilityToString(pair._2().access())))
                        .build())
                .collect(Collectors.toUnmodifiableList());

    }
}
