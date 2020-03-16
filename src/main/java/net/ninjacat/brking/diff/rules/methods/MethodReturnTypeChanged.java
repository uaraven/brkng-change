package net.ninjacat.brking.diff.rules.methods;

import java.util.List;
import java.util.stream.Collectors;

import io.vavr.Tuple;
import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

public class MethodReturnTypeChanged implements MethodDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiMethod> older,
                                     final ApiObjectPool<ApiMethod> newer) {
        final var matching = matchingOnly(older, newer);
        return matching.stream()
                .map(method -> Tuple.of(older.get(method.identifier()), newer.get(method.identifier())))
                .filter(pair -> !pair._1().returnType().equals(pair._2().returnType()))
                .map(pair -> ImmutableDiffElement.builder()
                        .ownerClass(reference)
                        .diffType(DiffType.MethodReturnTypeChanged)
                        .changedObject(pair._1())
                        .changedFrom(pair._1().returnType())
                        .changedTo(pair._2().returnType())
                        .build())
                .collect(Collectors.toUnmodifiableList());

    }

}


