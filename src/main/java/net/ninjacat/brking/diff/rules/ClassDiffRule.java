package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffElement;

import java.util.List;

public interface ClassDiffRule {
    List<DiffElement> process(ApiClass older, ApiClass newer);
}
