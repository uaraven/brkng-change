package net.ninjacat.brking.diff.rules.methods;

import java.util.List;
import java.util.stream.Collectors;

import io.vavr.Tuple;
import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import static net.ninjacat.brking.utils.AnnotationUtils.sameAnnotations;

public class MethodAnnotationsChanged implements MethodDiffRule {
  @Override
  public List<DiffElement> process(
          final ApiObject reference,
          final ApiObjectPool<ApiMethod> older,
          final ApiObjectPool<ApiMethod> newer,
          final DiffContext context) {

    final var matching = matchingOnly(older, newer);
    return matching.stream()
        .map(method -> Tuple.of(older.get(method.identifier()), newer.get(method.identifier())))
        .filter(pair -> !sameAnnotations(pair._1().annotations(), pair._2().annotations()))
            .map(pair -> ImmutableDiffElement.builder()
                    .ownerClass(reference)
                    .changedObject(pair._1())
                    .changedFrom(pair._1().annotationNames())
                    .changedTo(pair._2().annotationNames())
                                                    .diffType(DiffType.MethodAnnotationsChanged)
                                                    .build())
                   .collect(Collectors.toUnmodifiableList());
  }
}
