package net.ninjacat.brking.api;

import net.ninjacat.brking.utils.AsmUtils;
import org.immutables.value.Value;

@Value.Immutable
public interface ApiField extends ApiClassElement {

  @Override
  default String returnType() {
    return AsmUtils.descriptorToClass(descriptor());
  }

  @Override
  default String apiName() {
    return String.format("%s %s %s", AsmUtils.modifiersToString(access()), returnType(), name());
  }

  @Override
  default String apiDescription(final ApiObject owner) {
    return apiName() + " in " + owner.apiDescription(null);
  }
}
