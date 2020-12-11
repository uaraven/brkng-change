package net.ninjacat.brking.utils;

import java.util.List;
import java.util.stream.Collectors;

import net.ninjacat.brking.api.ApiAnnotation;
import net.ninjacat.brking.api.ApiObject;

public final class AnnotationUtils
{
  private AnnotationUtils() {}

  public static List<ApiAnnotation> filterOutDeprecated(final List<ApiAnnotation> annotations) {
    return annotations.stream()
        .filter(ann -> !ann.descriptor().equals("java.lang.Deprecated"))
        .collect(Collectors.toUnmodifiableList());
  }

  public static boolean hasDeprecated(final ApiObject element) {
    return element.annotations().stream()
        .anyMatch(ann -> ann.descriptor().equals("java.lang.Deprecated"));
  }
}
