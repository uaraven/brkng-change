package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;
import java.util.stream.Collectors;

public class MethodAdded implements MethodDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiMethod> older,
                                     final ApiObjectPool<ApiMethod> newer,
                                     final DiffContext context) {
        return newer.all().entrySet().stream()
                .filter(e -> !older.all().containsKey(e.getKey()))
                .map(e -> ImmutableDiffElement.builder()
                        .ownerClass(reference)
                        .diffType(DiffType.MethodAdded)
                        .changedObject(e.getValue())
                        .changedFrom("")
                        .changedTo(e.getValue().apiName())
                        .build())
                .collect(Collectors.toUnmodifiableList());

    }

}


