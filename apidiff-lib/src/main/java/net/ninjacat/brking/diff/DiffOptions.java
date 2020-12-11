package net.ninjacat.brking.diff;

import org.immutables.value.Value;

@Value.Immutable
public interface DiffOptions
{
    @Value.Default
    default boolean relaxedInheritance() {
        return false;
    }

    @Value.Default
    default boolean isPublicOnly() { return true; }
}
