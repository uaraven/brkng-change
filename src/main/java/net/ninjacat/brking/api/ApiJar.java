package net.ninjacat.brking.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import io.vavr.Tuple;
import net.ninjacat.brking.api.internal.ApiClassParser;

public final class ApiJar {
  private final Map<String, ApiClass> classes;

  public static ApiJar of(final JarFile file) {
    return new ApiJar(file);
  }

  private ApiJar(final JarFile jarFile) {
    classes = ImmutableMap.copyOf(jarFile.stream()
                                         .filter(entry -> !entry.isDirectory())
                                         .filter(entry -> entry.getRealName().endsWith(".class"))
                                         .map(entry -> entryToStream(jarFile, entry))
                                         .map(ApiClassParser::of)
                                         .filter(ApiClass::isPublic)
                                         .collect(Collectors.toConcurrentMap(ApiClass::name, Function.identity())));
  }

  public Map<String, ApiClass> getClasses() {
    return ImmutableMap.copyOf(classes);
  }

  public Optional<ApiClass> getClass(final String name) {
    return Optional.ofNullable(classes.getOrDefault(name, null));
  }

  private InputStream entryToStream(final JarFile jarFile, final JarEntry entry) {
    try {
      return jarFile.getInputStream(entry);
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public void findDiff(final ApiJar newer) {
    final Set<String> newClasses =
        newer.getClasses().keySet().stream()
             .filter(cls -> !classes.containsKey(cls))
             .collect(Collectors.toSet());

    final Set<String> existingClasses = newer.getClasses().keySet().stream()
                                             .filter(classes::containsKey)
                                             .collect(Collectors.toSet());

    existingClasses.stream()
                   .map(clsName -> Tuple.of(classes.get(clsName), newer.classes.get(clsName)))
                   .map(tpl -> tpl._1.diff(tpl._2));
  }
}
