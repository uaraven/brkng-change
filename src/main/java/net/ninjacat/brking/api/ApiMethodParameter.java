package net.ninjacat.brking.api;

import org.immutables.value.Value;

@Value.Immutable
public interface ApiMethodParameter {
  int access();

  String name();
}
