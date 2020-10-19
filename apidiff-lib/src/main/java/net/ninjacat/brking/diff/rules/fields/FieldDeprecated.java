package net.ninjacat.brking.diff.rules.fields;

import java.util.List;
import java.util.stream.Collectors;

import io.vavr.Tuple;
import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.api.ApiObjectPool;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import static net.ninjacat.brking.utils.AnnotationUtils.hasDeprecated;

public class FieldDeprecated
    implements FieldDiffRule
{
  @Override
  public List<DiffElement> process(
      final ApiObject reference,
      final ApiObjectPool<ApiField> older,
      final ApiObjectPool<ApiField> newer, final DiffContext context)
  {
    final var matching = matchingOnly(older, newer);
    return matching.stream()
        .map(field -> Tuple.of(older.get(field.name()), newer.get(field.name())))
        .filter(pair -> !hasDeprecated(pair._1()) && hasDeprecated(pair._2()))
        .map(pair -> ImmutableDiffElement.builder()
            .ownerClass(reference)
            .diffType(DiffType.FieldDeprecated)
            .changedObject(pair._1())
            .changedTo("Deprecated")
            .build())
        .collect(Collectors.toUnmodifiableList());
  }
}
