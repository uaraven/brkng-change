package net.ninjacat.apidiff.utils;

import java.util.Optional;

public final class StringUtils {
  private StringUtils() {
  }

  public static String defaultIfEmpty(final String value, final String defaultValue) {
    return Optional.ofNullable(value).orElse(defaultValue);
  }
}
