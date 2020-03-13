package net.ninjacat.brking.diff.rules;

import java.util.Collection;
import java.util.List;

import net.ninjacat.brking.api.ApiObject;

public class RuleProvider {
  Collection<DiffRule<? extends ApiObject>> CLASS_RULES = List.of(
      // Breaking
      new ClassAccess(),
      new ClassRemovingInterfaces(),
      new ClassChangingSuperclass(),
      // Warning
      new ClassVersion(),
      // Safe
      new ClassImplementingNewInterfaces()
  );
}
