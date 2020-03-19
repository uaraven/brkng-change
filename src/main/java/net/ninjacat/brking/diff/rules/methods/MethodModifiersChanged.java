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

public class MethodModifiersChanged implements MethodDiffRule {

    @Override
    public List<DiffElement> process(
            final ApiObject reference,
            final ApiObjectPool<ApiMethod> older,
            final ApiObjectPool<ApiMethod> newer,
            final DiffContext context) {
        final var matching = matchingOnly(older, newer);
        return matching.stream()
                .map(method -> Tuple.of(older.get(method.identifier()), newer.get(method.identifier())))
                .filter(pair -> AsmUtils.hasModifierChanged(pair._1(), pair._2()))
                .map(pair -> ImmutableDiffElement.builder()
                        .ownerClass(reference)
                        .changedObject(pair._1())
                        .changedFrom(AsmUtils.modifiersToString(pair._1().access()))
                        .changedTo(AsmUtils.modifiersToString(pair._2().access()))
                        .diffType(DiffType.MethodModifiersChanged)
                        .build())
                .collect(Collectors.toUnmodifiableList());

    }


    /* TODO: More detailed message depending on the type of modifiers changed

       synchronized/native - warning
       strictfp - ???
       final/static - breaking
       abstract - breaking

    */
}
