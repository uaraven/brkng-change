package net.ninjacat.brking.utils;

import java.util.Enumeration;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class StreamUtils {
  private StreamUtils() {
  }

  public static <T> Stream<T> enumerationAsStream(final Enumeration<T> enumeration) {
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(enumeration.asIterator(), Spliterator.ORDERED),
        false);
  }
}
