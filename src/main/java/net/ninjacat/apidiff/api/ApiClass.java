package net.ninjacat.apidiff.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import net.ninjacat.apidiff.api.diff.ChangeSeverity;
import net.ninjacat.apidiff.api.diff.DiffElement;
import net.ninjacat.apidiff.api.diff.ImmutableDiffElement;
import net.ninjacat.apidiff.utils.AsmUtils;
import org.immutables.value.Value;
import org.objectweb.asm.Opcodes;

import static net.ninjacat.apidiff.utils.AsmUtils.className;

@Value.Immutable
public interface ApiClass extends ApiObject {
  int version();

  int access();

  String name();

  @Nullable
  String signature();

  String superName();

  List<String> interfaces();

  List<ApiField> fields();

  List<ApiMethod> methods();

  default boolean isPublic() {
    return (access() & Opcodes.ACC_PUBLIC) != 0;
  }

  default List<DiffElement> diff(final ApiClass newer) {
    final var differences = new ArrayList<DiffElement>();
    diffVersion(newer, differences);
    diffAccess(newer, differences);
    diffSuperClass(newer, differences);
    diffLostInterfaces(newer, differences);
    diffObtainedInterfaces(newer, differences);

    diffLostFields(newer, differences);
    diffObtainedFields(newer, differences);

    return ImmutableList.copyOf(differences);
  }

  private void diffLostFields(final ApiClass newer, final ArrayList<DiffElement> differences) {
    final var lostFields = this.fieldNames();
    lostFields.removeAll(newer.fieldNames());
    if (!lostFields.isEmpty()) {
      lostFields.forEach(fname -> {
        final var field = newer.fields().stream().filter(f -> f.name().equals(fname)).findFirst().get();
        differences.add(ImmutableDiffElement.builder()
                                            .apiObject(this)
                                            .severity(ChangeSeverity.BREAKING)
                                            .description(
                                                "Field has been removed: %s".formatted(field.apiDescription(this)))
                                            .build()
        );
      });
    }
  }

  private void diffObtainedFields(final ApiClass newer, final ArrayList<DiffElement> differences) {
    final var newFields = newer.fieldNames();
    newFields.removeAll(fieldNames());
    if (!newFields.isEmpty()) {
      final var fieldNames = String.join(",", newFields);
      differences.add(ImmutableDiffElement.builder()
                                          .apiObject(this)
                                          .severity(ChangeSeverity.SAFE)
                                          .description(
                                              "New fields has been added: %s".formatted(fieldNames))
                                          .build()
      );
    }
  }

  private HashSet<String> fieldNames() {
    return fields().stream().map(ApiField::name).collect(Collectors.toCollection(HashSet::new));
  }

  private void diffLostInterfaces(final ApiClass newer, final ArrayList<DiffElement> differences) {
    final var olderInterfaces = new HashSet<>(this.interfaces());
    olderInterfaces.removeAll(newer.interfaces());
    if (!olderInterfaces.isEmpty()) {
      final var lostIntf = olderInterfaces.stream().map(AsmUtils::className).collect(Collectors.joining(","));
      differences.add(ImmutableDiffElement.builder()
                                          .apiObject(this)
                                          .severity(ChangeSeverity.BREAKING)
                                          .description(
                                              "Some interfaces are no longer implemented by this class: " + lostIntf)
                                          .build());
    }
  }

  private void diffObtainedInterfaces(final ApiClass newer, final ArrayList<DiffElement> differences) {
    final var newerInterfaces = new HashSet<>(newer.interfaces());
    newerInterfaces.removeAll(this.interfaces());
    if (!newerInterfaces.isEmpty()) {
      final var newIntf = newerInterfaces.stream().map(AsmUtils::className).collect(Collectors.joining(","));
      differences.add(ImmutableDiffElement.builder()
                                          .apiObject(this)
                                          .severity(ChangeSeverity.SAFE)
                                          .description(
                                              "New interfaces implemented by this class: " + newIntf)
                                          .build());
    }
  }

  private void diffSuperClass(final ApiClass newer, final ArrayList<DiffElement> differences) {
    if (!newer.superName().equals(this.superName())) {
      differences.add(ImmutableDiffElement.builder()
                                          .apiObject(this)
                                          .severity(ChangeSeverity.POTENTIALLY_BREAKING)
                                          .description(
                                              "Superclass of '%s' has changed from '%s' to '%s'. That may be a breaking change if this is not merely renaming or new class added to existing hierarchy"
                                                  .formatted(className(name()), className(this.superName()),
                                                             className(newer.superName())))
                                          .build());
    }
  }

  private void diffAccess(final ApiClass newer, final ArrayList<DiffElement> differences) {
    final var stricterAccessWarning = AsmUtils.stricterAccessWarning(newer.access(), this.access());
    stricterAccessWarning.ifPresent(warning -> {
      differences.add(ImmutableDiffElement.builder()
                                          .apiObject(this)
                                          .severity(ChangeSeverity.BREAKING)
                                          .description(warning)
                                          .build());
    });
  }

  private void diffVersion(final ApiClass newer, final ArrayList<DiffElement> differences) {
    if (newer.version() < this.version()) {
      differences.add(ImmutableDiffElement.builder()
                                          .apiObject(this)
                                          .severity(ChangeSeverity.WARNING)
                                          .description(
                                              "Bytecode version has changed from %d to %d. Newer version may require newer JDK to run"
                                                  .formatted(this.version(), newer.version()))
                                          .build());
    }
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
