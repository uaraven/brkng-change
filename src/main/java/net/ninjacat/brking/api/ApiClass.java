package net.ninjacat.brking.api;

import java.util.List;

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

  @Override
  default String apiName() {
    return className(name());
  }

  @Override
  default String apiDescription(final ApiObject owner) {
    return "class " + apiName();
  }
}
