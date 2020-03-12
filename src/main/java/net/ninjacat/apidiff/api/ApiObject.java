package net.ninjacat.apidiff.api;

public interface ApiObject {
  String apiName();

  String apiDescription(ApiObject owner);
}
