package net.ninjacat.brking.diff.rules;

import java.util.Collection;

import net.ninjacat.brking.api.ApiObject;
import net.ninjacat.brking.diff.DiffElement;

public interface DiffRule<T extends ApiObject> {
  Collection<DiffElement> process(T older, T newer);
}
