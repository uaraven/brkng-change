package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MethodDiffRule {
    List<DiffElement> process(ApiObject reference,
                              ApiObjectPool<ApiMethod> older,
                              ApiObjectPool<ApiMethod> newer,
                              DiffContext context);

    default Collection<ApiMethod> matchingOnly(final ApiObjectPool<ApiMethod> older, final ApiObjectPool<ApiMethod> newer) {
        return older.all().entrySet()
                .stream()
                .filter(entry -> newer.all().containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}
