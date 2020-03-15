package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;
import java.util.stream.Collectors;

public class MethodRemoved implements MethodDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiMethod> older,
                                     final ApiObjectPool<ApiMethod> newer) {
        return older.all().entrySet().stream()
                .filter(e -> !newer.all().containsKey(e.getKey()))
                .map(e -> ImmutableDiffElement.builder()
                        .ownerClass(reference)
                        .severity(ChangeSeverity.BREAKING)
                        .description(String.format("Method '%s' was removed",
                                e.getValue().apiName()))
                        .build())
                .collect(Collectors.toUnmodifiableList());

    }

}


