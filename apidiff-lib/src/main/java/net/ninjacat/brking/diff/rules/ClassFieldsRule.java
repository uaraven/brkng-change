package net.ninjacat.brking.diff.rules;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffContext;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.rules.fields.FieldAdded;
import net.ninjacat.brking.diff.rules.fields.FieldAnnotationsChanged;
import net.ninjacat.brking.diff.rules.fields.FieldDeprecated;
import net.ninjacat.brking.diff.rules.fields.FieldDiffRule;
import net.ninjacat.brking.diff.rules.fields.FieldModifiersChanged;
import net.ninjacat.brking.diff.rules.fields.FieldPool;
import net.ninjacat.brking.diff.rules.fields.FieldRemoved;
import net.ninjacat.brking.diff.rules.fields.FieldTypeChanged;
import net.ninjacat.brking.diff.rules.fields.FieldVisibilityChanged;

public class ClassFieldsRule implements ClassDiffRule {

    private static final List<FieldDiffRule> fieldRules = List.of(
        new FieldModifiersChanged(),
        new FieldVisibilityChanged(),
        new FieldRemoved(),
        new FieldTypeChanged(),
        new FieldAdded(),
        new FieldDeprecated(),
        new FieldAnnotationsChanged()
    );

    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer, final DiffContext context) {
        final var olderFields = FieldPool.of(older);
        final var newerFields = FieldPool.of(newer);
        final List<DiffElement> diffResults = new ArrayList<>();

        fieldRules.forEach(rule -> diffResults.addAll(rule.process(older, olderFields, newerFields, context)));

        return ImmutableList.copyOf(diffResults);
    }
}
