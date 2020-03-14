package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffElement;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface FieldDiffRule {
    List<DiffElement> process(final ApiObject reference,
                              final ApiObjectPool<ApiField> older, final ApiObjectPool<ApiField> newer);

    default Collection<ApiField> matchingOnly(final ApiObjectPool<ApiField> older, final ApiObjectPool<ApiField> newer) {
        return older.all().entrySet()
                .stream()
                .filter(entry -> newer.all().containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}
