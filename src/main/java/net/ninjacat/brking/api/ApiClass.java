package net.ninjacat.brking.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.immutables.value.Value;
import org.objectweb.asm.Opcodes;

import static net.ninjacat.brking.utils.AsmUtils.className;

@Value.Immutable
public interface ApiClass extends ApiObject {
  int version();

  @Override
  default String descriptor() {
    return name();
  }

  String superName();

  List<String> interfaces();

  List<ApiField> fields();

  List<ApiMethod> methods();

  default boolean isPublic() {
    return (access() & Opcodes.ACC_PUBLIC) != 0;
  }

  //
  //private void diffLostFields(final ApiClass newer, final ArrayList<DiffElement> differences) {
  //  final var lostFields = this.fieldNames();
  //  lostFields.removeAll(newer.fieldNames());
  //  if (!lostFields.isEmpty()) {
  //    lostFields.forEach(fname -> {
  //      final var field = newer.fields().stream().filter(f -> f.name().equals(fname)).findFirst().get();
  //      differences.add(ImmutableDiffElement.builder()
  //                                          .apiObject(this)
  //                                          .severity(ChangeSeverity.BREAKING)
  //                                          .description(
  //                                              String.format("Field has been removed: %s", field.apiDescription(this)))
  //                                          .build()
  //      );
  //    });
  //  }
  //}
  //
  //private void diffObtainedFields(final ApiClass newer, final ArrayList<DiffElement> differences) {
  //  final var newFields = newer.fieldNames();
  //  newFields.removeAll(fieldNames());
  //  if (!newFields.isEmpty()) {
  //    final var fieldNames = String.join(",", newFields);
  //    differences.add(ImmutableDiffElement.builder()
  //                                        .apiObject(this)
  //                                        .severity(ChangeSeverity.SAFE)
  //                                        .description(
  //                                            String.format("New fields has been added: %s", fieldNames))
  //                                        .build()
  //    );
  //  }
  //}
  //
  private Set<String> fieldNames() {
    return fields().stream().map(ApiField::name).collect(Collectors.toCollection(HashSet::new));
  }

  @Override
  default String apiName() {
    return className(name());
  }

  @Override
  default String apiDescription(final ApiObject owner) {
    return "class " + apiName();
  }
}
