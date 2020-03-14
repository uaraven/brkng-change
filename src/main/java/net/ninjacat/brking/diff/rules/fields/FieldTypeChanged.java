package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;
import net.ninjacat.brking.utils.AsmUtils;

import java.util.List;
import java.util.stream.Collectors;

public class FieldTypeChanged implements FieldDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiField> older,
                                     final ApiObjectPool<ApiField> newer) {
        final var olderFields = older.all();
        final var newerFields = newer.all();

        return olderFields.keySet().stream()
                .filter(newerFields::containsKey)
                .filter(name -> !olderFields.get(name).signature().equals(newerFields.get(name).signature()))
                .map(field -> ImmutableDiffElement
                        .builder()
                        .apiObject(reference)
                        .description(
                                String.format("Field '%s' type has changed to '%s'",
                                        olderFields.get(field).apiName(),
                                        AsmUtils.className(newerFields.get(field).descriptor())))
                        .severity(ChangeSeverity.BREAKING)
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }
}
