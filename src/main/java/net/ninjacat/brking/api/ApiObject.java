package net.ninjacat.brking.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

public interface ApiObject {
  int access();

  String name();

  String descriptor();

  @Nullable
  String signature();

  List<ApiAnnotation> annotations();

  String apiName();

  String apiDescription(ApiObject owner);

  default String annotationNames() {
    return annotations().stream()
                        .map(ApiAnnotation::descriptor)
                        .collect(Collectors.joining(","));
  }
}
