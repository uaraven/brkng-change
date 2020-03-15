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

public class MethodExceptionsChanged implements MethodDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiMethod> older,
                                     final ApiObjectPool<ApiMethod> newer) {
        final var matching = matchingOnly(older, newer);
        return matching.stream()
                .map(method -> Tuple.of(older.get(method.identifier()), newer.get(method.identifier())))
                .filter(pair -> !pair._1().exceptions().equals(pair._2().exceptions()))
                .map(pair -> ImmutableDiffElement.builder()
                        .apiObject(reference)
                        .severity(ChangeSeverity.BREAKING)
                        .description(String.format("Method '%s' exceptions changed to '%s'",
                                pair._1().apiName(),
                                String.join(",", pair._2().exceptions())))
                        .build())
                .collect(Collectors.toUnmodifiableList());

    }

}
