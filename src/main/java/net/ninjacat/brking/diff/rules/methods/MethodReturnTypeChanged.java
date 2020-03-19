package net.ninjacat.brking.diff.rules.methods;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;
import java.util.stream.Collectors;

public class MethodReturnTypeChanged implements MethodDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiMethod> older,
                                     final ApiObjectPool<ApiMethod> newer,
                                     final DiffContext context) {
        final var matching = matchingOnly(older, newer);
        return matching.stream()
                .map(method -> Tuple.of(older.get(method.identifier()), newer.get(method.identifier())))
                .filter(pair -> !pair._1().returnType().equals(pair._2().returnType()))
                .map(pair -> buildDiff(reference, pair, context))
                .collect(Collectors.toUnmodifiableList());

    }

    public ImmutableDiffElement buildDiff(final ApiObject reference,
                                          final Tuple2<ApiMethod, ApiMethod> pair,
                                          final DiffContext context) {


        final var diffType = context.options().relaxedInheritance()
                && context.newerPool().forClass(pair._2().returnType())
                .map(it -> it.isSubclassOf(pair._1().returnType())).orElse(false)
                ? DiffType.MethodReturnTypeChangedToSubclass
                : DiffType.MethodReturnTypeChanged;

        return ImmutableDiffElement.builder()
                .ownerClass(reference)
                .diffType(diffType)
                .changedObject(pair._1())
                .changedFrom(pair._1().returnType())
                .changedTo(pair._2().returnType())
                .build();
    }

}


