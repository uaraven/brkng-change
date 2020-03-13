package net.ninjacat.brking.api;

import net.ninjacat.brking.utils.AsmUtils;
import org.immutables.value.Value;

@Value.Immutable
public interface ApiField extends ApiObject {

  @Override
  default String apiName() {
    return "%s %s %s".formatted(AsmUtils.modifiers(access()), AsmUtils.className(descriptor()), name());
  }

  @Override
  default String apiDescription(final ApiObject owner) {
    return apiName() + " in " + owner.apiDescription(null);
  }
}
