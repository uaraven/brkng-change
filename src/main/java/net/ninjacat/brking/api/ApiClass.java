package net.ninjacat.brking.api;

import org.immutables.value.Value;
import org.objectweb.asm.Opcodes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
