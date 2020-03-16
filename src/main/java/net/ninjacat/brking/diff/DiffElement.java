package net.ninjacat.brking.diff;

import net.ninjacat.brking.api.ApiObject;
import org.immutables.value.Value.Immutable;

import javax.annotation.Nullable;
import java.util.Map;

@Immutable
public interface DiffElement {
  ApiObject ownerClass();

    @Nullable
    String description();

    DiffType diffType();

  /**
   * What object has changed - class, field or method
   *
   * @return {@link ApiObject}
   */
  ApiObject changedObject();

  /**
   * String representation of what has changed, will be available as ${changed.from}
   * in the templating context
   */
  @Nullable
  String changedFrom();

  /**
   * String representation of what has changed, will be available as ${changed.to}
   * in the templating context
   */
  String changedTo();

  default Map<String, Object> asTemplateContext() {
    return Map.of(
            "object", changedObject(),
            "object.name", changedObject().name(),
            "object.apiName", changedObject().apiName(),
            "owner.class", ownerClass().apiName(),
            "changed.from", changedFrom(),
            "changed.to", changedTo()
    );
  }
}
