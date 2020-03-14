package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;
import java.util.stream.Collectors;

public class FieldRemoved implements FieldDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiField> older,
                                     final ApiObjectPool<ApiField> newer) {
        final var olderFields = older.all();
        final var newerFields = newer.all();

        final var removed = olderFields.keySet().stream()
                .filter(it -> !newerFields.containsKey(it))
                .collect(Collectors.toUnmodifiableList());

        return removed.stream()
                .map(field -> ImmutableDiffElement
                        .builder()
                        .apiObject(reference)
                        .description(
                                String.format("Field '%s' has been removed", olderFields.get(field).apiName()))
                        .severity(ChangeSeverity.BREAKING)
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }
}
