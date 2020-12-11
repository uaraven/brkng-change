package net.ninjacat.brking.utils;

import java.util.Optional;

import javax.annotation.Nullable;

public final class StringUtils
{
  private StringUtils() {
  }

  public static boolean isBlank(@Nullable final String text) {
    return text == null || text.isBlank();
  }

  public static boolean isNotBlank(@Nullable final String text) {
    return text != null && !text.isBlank();
  }

  public static String defaultIfEmpty(final String value, final String defaultValue) {
    return Optional.ofNullable(value).orElse(defaultValue);
  }
}
