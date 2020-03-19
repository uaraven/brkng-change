package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FieldTypeChanged implements FieldDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiField> older,
                                     final ApiObjectPool<ApiField> newer,
                                     final DiffContext context) {
        final var olderFields = older.all();
        final var newerFields = newer.all();

        return olderFields.keySet().stream()
                .filter(newerFields::containsKey)
                .filter(name -> !olderFields.get(name).descriptor().equals(newerFields.get(name).descriptor()))
                .map(field -> buildDiff(reference, olderFields, newerFields, field, context))
                .collect(Collectors.toUnmodifiableList());
    }

    public ImmutableDiffElement buildDiff(
            final ApiObject reference, final Map<String, ApiField> olderFields,
            final Map<String, ApiField> newerFields, final String field, final DiffContext context) {

        final var olderField = olderFields.get(field);
        final var newerField = newerFields.get(field);
        final var isNewerASubclassOfOlder = context.options().relaxedInheritance()
                && context.newerPool().forClass(newerField.returnType())
                .map(it -> it.isSubclassOf(olderField.returnType())).orElse(false);

        return ImmutableDiffElement
                .builder()
                .ownerClass(reference)
                .changedObject(olderField)
                .changedFrom(olderField.returnType())
                .changedTo(newerField.returnType())
                .diffType(isNewerASubclassOfOlder ? DiffType.FieldTypeChangedToSubclass : DiffType.FieldTypeChanged)
                .build();
    }
}
