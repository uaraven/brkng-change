package net.ninjacat.brking.diff;

import java.util.List;

import javax.annotation.Nullable;

import net.ninjacat.brking.api.ApiObject;
import org.immutables.value.Value.Immutable;

@Immutable
public interface DiffElement {
  ApiObject apiObject();

  String description();

  ChangeSeverity severity();

  @Nullable
  List<DiffElement> children();
}
