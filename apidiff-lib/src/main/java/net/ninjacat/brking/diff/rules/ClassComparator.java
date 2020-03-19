package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ClassComparator {
  private static final Collection<ClassDiffRule> CLASS_RULES = List.of(
      // Breaking
      new ClassVisibilityChanged(),
      new ClassModifiersChanged(),
      new ClassInterfacesRemoved(),
      new ClassSuperclassChanged(),
      new ClassFieldsRule(),
      new ClassMethodsRule(),
      // Warning
      new ClassVersion(),
      // Safe
      new ClassInterfacesAdded(),
      new ClassAnnotationsChanged()
  );

  public static List<DiffElement> compare(final ApiClass older, final ApiClass newer, final DiffContext context) {
    return CLASS_RULES.stream()
            .flatMap(rule -> rule.process(older, newer, context).stream())
            .collect(Collectors.toUnmodifiableList());
  }
}
