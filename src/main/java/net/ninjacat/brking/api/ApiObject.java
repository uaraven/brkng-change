package net.ninjacat.brking.api;

import javax.annotation.Nullable;

public interface ApiObject {
  int access();

  String name();

  String descriptor();

  @Nullable
  String signature();

  String apiName();

  String apiDescription(ApiObject owner);
}
