package net.ninjacat.brking.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.ninjacat.brking.api.ApiAnnotation;
import net.ninjacat.brking.api.ApiObject;

public final class AnnotationUtils
{
  private AnnotationUtils() {}

  public static boolean sameAnnotations(final List<ApiAnnotation> older, final List<ApiAnnotation> newer) {
    return filterOutDeprecated(older).equals(filterOutDeprecated(newer));
  }

  public static Set<ApiAnnotation> filterOutDeprecated(final List<ApiAnnotation> annotations) {
    return annotations.stream()
        .filter(ann -> !ann.descriptor().equals("java.lang.Deprecated"))
        .collect(Collectors.toUnmodifiableSet());
  }

  public static boolean hasDeprecated(final ApiObject element) {
    return element.annotations().stream()
        .anyMatch(ann -> ann.descriptor().equals("java.lang.Deprecated"));
  }
}
