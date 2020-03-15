package net.ninjacat.brking.diff.rules.methods;

import io.vavr.Tuple;
import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;
import net.ninjacat.brking.utils.AsmUtils;

import java.util.List;
import java.util.stream.Collectors;

public class MethodModifiersChanged implements MethodDiffRule {

    @Override
    public List<DiffElement> process(final ApiObject reference,
                                     final ApiObjectPool<ApiMethod> older,
                                     final ApiObjectPool<ApiMethod> newer) {
        final var matching = matchingOnly(older, newer);
        return matching.stream()
                .map(method -> Tuple.of(older.get(method.identifier()), newer.get(method.identifier())))
                .filter(pair -> AsmUtils.hasModifierChanged(pair._1(), pair._2()))
                .map(pair -> ImmutableDiffElement.builder()
                        .ownerClass(reference)
                        .severity(ChangeSeverity.BREAKING)
                        .description(String.format("Method '%s' modifiers changed to '%s'",
                                pair._1().apiName(),
                                AsmUtils.modifiersToString(pair._2().access())))
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
