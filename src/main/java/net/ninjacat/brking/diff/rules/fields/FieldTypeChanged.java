package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

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
                .filter(name -> !olderFields.get(name).descriptor().equals(newerFields.get(name).descriptor()))
                .map(field -> ImmutableDiffElement
                        .builder()
                        .ownerClass(reference)
                        .changedObject(olderFields.get(field))
                        .changedFrom(olderFields.get(field).returnType())
                        .changedTo(newerFields.get(field).returnType())
                        .diffType(DiffType.FieldTypeChanged)
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }
}
