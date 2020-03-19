package net.ninjacat.brking.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApiObjectPool<T> {

    private final Map<String, T> objects;

    protected ApiObjectPool(final List<T> objects, final Function<T, String> nameExtractor) {
        this.objects = objects.stream().collect(Collectors.toUnmodifiableMap(nameExtractor, Function.identity()));
    }

    public Map<String, T> all() {
        return objects;
    }

    public boolean has(final String name) {
        return objects.containsKey(name);
    }

    public Optional<T> find(final String name) {
        return Optional.ofNullable(objects.get(name));
    }

    public T get(final String name) {
        return objects.get(name);
    }

}
