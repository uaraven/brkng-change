package net.ninjacat.brking.diff.rules;

import java.util.Collection;
import java.util.List;

public class RuleProvider {
  Collection<ClassDiffRule> CLASS_RULES = List.of(
          // Breaking
          new ClassVisibilityChanged(),
          new ClassModifiersChanged(),
          new ClassInterfacesRemoved(),
          new ClassSuperclassChanged(),
          new ClassFieldsRule(),
          // Warning
          new ClassVersion(),
          // Safe
          new ClassImplementingNewInterfaces()
  );
}
