package net.ninjacat.brking.diff.rules;

import com.google.common.collect.ImmutableList;
import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.rules.fields.*;

import java.util.ArrayList;
import java.util.List;

public class ClassFieldsRule implements ClassDiffRule {

    private static final List<FieldDiffRule> fieldRules = List.of(
            new FieldModifiersChanged(),
            new FieldVisibilityChanged(),
            new FieldRemoved(),
            new FieldTypeChanged(),
            new FieldAdded()
    );

    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer) {
        final var olderFields = FieldPool.of(older);
        final var newerFields = FieldPool.of(newer);
        final List<DiffElement> diffResults = new ArrayList<>();

        fieldRules.forEach(rule -> diffResults.addAll(rule.process(older, olderFields, newerFields)));

        return ImmutableList.copyOf(diffResults);
    }
}
