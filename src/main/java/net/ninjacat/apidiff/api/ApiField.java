package net.ninjacat.apidiff.api;

import javax.annotation.Nullable;

import net.ninjacat.apidiff.utils.AsmUtils;
import org.immutables.value.Value;

@Value.Immutable
public interface ApiField extends ApiObject {
  int access();

  String name();

  String descriptor();

  @Nullable
  String signature();

  @Override
  default String apiName() {
    return "%s %s %s".formatted(AsmUtils.modifiers(access()), AsmUtils.className(descriptor()), name());
  }

  @Override
  default String apiDescription(final ApiObject owner) {
    return apiName() + " in " + owner.apiDescription(null);
  }
}
