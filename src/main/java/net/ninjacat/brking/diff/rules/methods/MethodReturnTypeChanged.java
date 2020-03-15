package net.ninjacat.brking.diff.rules.methods;

import io.vavr.Tuple;
import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;
import java.util.stream.Collectors;

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
                        .apiObject(reference)
                        .severity(ChangeSeverity.BREAKING)
                        .description(String.format("Method '%s' return type changed to '%s'",
                                pair._1().apiName(),
                                pair._2().returnType()))
                        .build())
                .collect(Collectors.toUnmodifiableList());

    }

}


