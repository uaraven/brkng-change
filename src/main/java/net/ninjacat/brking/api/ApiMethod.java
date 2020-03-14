package net.ninjacat.brking.api;

import net.ninjacat.brking.utils.AsmUtils;
import org.immutables.value.Value;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Value.Immutable
public interface ApiMethod extends ApiObject {
  List<ApiMethodParameter> parameters();

  List<String> exceptions();

  @Override
  default String apiName() {
    final var m = new Method(name(), descriptor());

    final var params = Arrays.stream(m.getArgumentTypes())
                             .map(Type::getClassName)
                             .collect(Collectors.joining(","));

    return "%s %s %s(%s)".formatted(AsmUtils.modifiersToString(access()),
            m.getReturnType().getClassName(),
            name(),
            params);
  }

  @Override
  default String apiDescription(final ApiObject owner) {
    return apiName() + " in " + owner.apiDescription(null);
  }
}
