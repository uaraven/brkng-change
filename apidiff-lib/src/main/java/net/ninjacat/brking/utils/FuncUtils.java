package net.ninjacat.brking.utils;

import java.util.Optional;
import java.util.function.Supplier;

public final class FuncUtils {
  private FuncUtils() {
  }

  /**
   * Executes a {@link Runnable} if the condition is true
   *
   * @param condition boolean condition
   * @param block     Runnable to execute
   */
  public static void whenTrue(final boolean condition, final Runnable block) {
    if (condition) {
      block.run();
    }
  }

  /**
   * Executes a {@link Runnable} if the condition is true
   *
   * @param condition boolean condition
   * @param block     Runnable to execute
   */
  public static void whenTrueThrow(final boolean condition, final Supplier<RuntimeException> block) {
    if (condition) {
      throw block.get();
    }
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static <T> Optional<T> firstNonEmpty(final Optional<T> a, final Optional<T> b) {
    return a.isPresent() ? a : b;
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static <T> T getFirst(final Optional<T> a, final Optional<T> b) {
    return a.orElseGet(b::orElseThrow);
  }
}
