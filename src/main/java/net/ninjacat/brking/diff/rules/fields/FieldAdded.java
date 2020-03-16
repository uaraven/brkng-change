package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;
import java.util.stream.Collectors;

public class FieldAdded implements FieldDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiField> older,
                                     final ApiObjectPool<ApiField> newer) {
        final var olderFields = older.all();
        final var newerFields = newer.all();

        final var removed = newerFields.keySet().stream()
                .filter(it -> !olderFields.containsKey(it))
                .collect(Collectors.toUnmodifiableList());

        return removed.stream()
                .map(field -> ImmutableDiffElement
                        .builder()
                        .ownerClass(reference)
                        .changedObject(newerFields.get(field))
                        .changedFrom("")
                        .changedTo(newerFields.get(field).apiName())
                        .diffType(DiffType.FieldAdded)
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }
}
