package net.ninjacat.brking.diff;

import net.ninjacat.brking.api.ClassPool;
import org.immutables.value.Value;

@Value.Immutable
public interface DiffContext {
    ClassPool olderPool();

    ClassPool newerPool();

    DiffOptions options();

    static ImmutableDiffContext empty() {
        return ImmutableDiffContext.builder()
                .newerPool(ClassPool.empty())
                .olderPool(ClassPool.empty())
                .options(ImmutableDiffOptions.builder().build())
                .build();
    }
}
