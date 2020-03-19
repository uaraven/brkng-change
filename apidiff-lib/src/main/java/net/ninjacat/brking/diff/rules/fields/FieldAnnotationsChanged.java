package net.ninjacat.brking.diff.rules.fields;

import io.vavr.Tuple;
import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;
import java.util.stream.Collectors;

public class FieldAnnotationsChanged implements FieldDiffRule {
  @Override
  public List<DiffElement> process(final ApiObject reference,
                                   final ApiObjectPool<ApiField> older,
                                   final ApiObjectPool<ApiField> newer, final DiffContext context) {
    final var matching = matchingOnly(older, newer);
    return matching.stream()
            .map(field -> Tuple.of(older.get(field.name()), newer.get(field.name())))
            .filter(pair -> !pair._1().annotations().equals(pair._2().annotations()))
            .map(pair -> ImmutableDiffElement.builder()
                    .ownerClass(reference)
                    .diffType(DiffType.FieldAnnotationsChanged)
                    .changedObject(pair._1())
                    .changedFrom(pair._1().annotationNames())
                    .changedTo(pair._2().annotationNames())
                                                    .build())
                   .collect(Collectors.toUnmodifiableList());
  }
}
