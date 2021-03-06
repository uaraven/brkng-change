package net.ninjacat.brking.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import net.ninjacat.brking.api.internal.ApiClassParser;

public final class ClassPool extends ApiObjectPool<ApiClass> {
    public ClassPool(final List<ApiClass> classes) {
        super(classes, ApiObject::name);
    }

    private ClassPool(final List<ApiClass> classes, final Function<ApiClass, String> nameExtractor) {
        super(classes, nameExtractor);
    }

  public static ClassPool empty() {
    return new ClassPool(List.of(), ApiClass::name);
  }

  public static ClassPool ofClass(final String className, final boolean publicOnly) {
    return new ClassPool(List.of(
        ApiClassParser.of(className, publicOnly).orElseThrow(() -> new IllegalArgumentException("Invalid class"))),
        cls -> "name");
  }

  public static ClassPool ofOlder(final JarFile file, final boolean publicOnly) {
    return of(file, publicOnly);
  }

  public static ClassPool ofNewer(final JarFile file, final boolean publicOnly) {
    return of(file, publicOnly);
  }

  public static ClassPool of(final JarFile file, final boolean publicOnly) {
    return new ClassPool(
        file.stream()
            .filter(entry -> !entry.isDirectory())
            .filter(entry -> entry.getRealName().endsWith(".class"))
            .map(entry -> entryToStream(file, entry))
            .flatMap(stream -> ApiClassParser.of(stream, publicOnly).stream())
            .filter(cls -> !publicOnly || cls.isPublic())
            .collect(Collectors.toList()));
  }

    private static InputStream entryToStream(final JarFile jarFile, final JarEntry entry) {
        try {
            return jarFile.getInputStream(entry);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Optional<PooledClass> forClass(final String type) {
        return find(type).map(it -> ImmutablePooledClass.of(it, this));
    }
}
