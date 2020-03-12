package net.ninjacat.apidiff.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.ninjacat.apidiff.utils.AsmUtils;
import org.immutables.value.Value;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

@Value.Immutable
public interface ApiMethod extends ApiObject {
  int access();

  String name();

  String descriptor();

  @Nullable
  String signature();

  List<ApiMethodParameter> parameters();

  List<String> exceptions();

  @Override
  default String apiName() {
    final var m = new Method(name(), descriptor());

    final var params = Arrays.stream(m.getArgumentTypes())
                             .map(Type::getClassName)
                             .collect(Collectors.joining(","));

    return "%s %s %s(%s)".formatted(AsmUtils.modifiers(access()),
                                    m.getReturnType().getClassName(),
                                    name(),
                                    params);
  }

  @Override
  default String apiDescription(final ApiObject owner) {
    return apiName() + " in " + owner.apiDescription(null);
  }
}
