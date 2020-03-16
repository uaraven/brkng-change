package net.ninjacat.brking.diff;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.Strings;
import net.ninjacat.brking.api.ApiObject;
import org.immutables.value.Value.Immutable;

@Immutable
public abstract class DiffElement {
  public abstract ApiObject ownerClass();

  @Nullable
  public abstract String description();

  public abstract DiffType diffType();

  /**
   * What object has changed - class, field or method
   *
   * @return {@link ApiObject}
   */
  public abstract ApiObject changedObject();

  /**
   * String representation of what has changed, will be available as ${changed.from} in the templating context
   */
  @Nullable
  public abstract String changedFrom();

  /**
   * String representation of what has changed, will be available as ${changed.to} in the templating context
   */
  public abstract String changedTo();

  @Override
  public String toString() {
    return String.format("%s: %s. %s changed: '%s' -> '%s'. %s",
                         diffType().getSeverity(), diffType(), changedObject().apiName(),
                         Strings.isNullOrEmpty(changedFrom()) ? "<none>" : changedFrom(),
                         Strings.isNullOrEmpty(changedTo()) ? "<none>" : changedTo(),
                         (Strings.isNullOrEmpty(description()) ? "" : "(" + description() + ")"));
  }

  public Map<String, Object> asTemplateContext() {
    return Map.of(
        "object", changedObject(),
        "object.name", changedObject().name(),
        "object.apiName", changedObject().apiName(),
        "owner.class", ownerClass().apiName(),
        "changed.from", Optional.ofNullable(changedFrom()).orElse(""),
        "changed.to", changedTo()
    );
  }
}
