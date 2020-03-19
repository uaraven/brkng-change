package net.ninjacat.brking.diff.rules.methods;

import io.vavr.Tuple;
import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;
import net.ninjacat.brking.utils.AsmUtils;

import java.util.List;
import java.util.stream.Collectors;

public class MethodVisibilityChanged implements MethodDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiMethod> older,
                                     final ApiObjectPool<ApiMethod> newer,
                                     final DiffContext context) {
        final var matching = matchingOnly(older, newer);
        return matching.stream()
                .map(method -> Tuple.of(older.get(method.identifier()), newer.get(method.identifier())))
                .filter(pair -> AsmUtils.hasAccessChangedToStricter(pair._1().access(), pair._2().access()))
                .map(pair -> ImmutableDiffElement.builder()
                        .ownerClass(reference)
                        .diffType(DiffType.MethodVisibilityChanged)
                        .changedObject(pair._1())
                        .changedFrom(AsmUtils.visibilityToString(pair._1().access()))
                        .changedTo(AsmUtils.visibilityToString(pair._2().access()))
                        .build())
                .collect(Collectors.toUnmodifiableList());

    }
}
