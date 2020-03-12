package net.ninjacat.apidiff.api;

import org.immutables.value.Value;

@Value.Immutable
public interface ApiAnnotation {
    @Value.Parameter(order = 0)
    String name();

    @Value.Parameter(order = 1)
    Object value();
}
