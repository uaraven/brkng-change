package net.ninjacat.brking.api;

import org.immutables.value.Value;
import org.immutables.value.Value.Parameter;

@Value.Immutable
public interface ApiAnnotation {
  @Parameter(order = 1)
  String descriptor();

  @Parameter(order = 2)
  boolean visible();
}
