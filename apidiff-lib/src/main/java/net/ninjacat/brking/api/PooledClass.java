package net.ninjacat.brking.api;

import org.immutables.value.Value;

import java.util.Optional;


@Value.Immutable
public interface PooledClass {

    String OBJECT = "java.lang.Object";

    @Value.Parameter(order = 1)
    ApiClass type();

    @Value.Parameter(order = 2)
    ClassPool pool();

    default boolean isSubclassOf(final String type) {
        var thisType = type();
        while (!OBJECT.equals(thisType.name())) {
            if (thisType.name().equals(type)) {
                return true;
            }
            final Optional<ApiClass> superType = pool().find(thisType.superName());
            if (superType.isEmpty()) {
                return false;
            }
            thisType = superType.get();
        }
        return false;
    }

}
