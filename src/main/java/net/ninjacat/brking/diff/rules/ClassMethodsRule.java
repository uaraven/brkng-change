package net.ninjacat.brking.diff.rules;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.rules.methods.MethodAdded;
import net.ninjacat.brking.diff.rules.methods.MethodAnnotationsChanged;
import net.ninjacat.brking.diff.rules.methods.MethodDiffRule;
import net.ninjacat.brking.diff.rules.methods.MethodExceptionsChanged;
import net.ninjacat.brking.diff.rules.methods.MethodModifiersChanged;
import net.ninjacat.brking.diff.rules.methods.MethodPool;
import net.ninjacat.brking.diff.rules.methods.MethodRemoved;
import net.ninjacat.brking.diff.rules.methods.MethodReturnTypeChanged;
import net.ninjacat.brking.diff.rules.methods.MethodVisibilityChanged;

public class ClassMethodsRule implements ClassDiffRule {

    private static final List<MethodDiffRule> methodRules = List.of(
        new MethodVisibilityChanged(),
        new MethodModifiersChanged(),
        new MethodReturnTypeChanged(),
        new MethodRemoved(),
        new MethodExceptionsChanged(),
        new MethodAdded(),
        new MethodAnnotationsChanged()
    );

    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer) {
        final var olderMethods = MethodPool.of(older);
        final var newerMethods = MethodPool.of(newer);

        final List<DiffElement> diffResults = new ArrayList<>();

        methodRules.forEach(rule -> diffResults.addAll(rule.process(older, olderMethods, newerMethods)));

        return ImmutableList.copyOf(diffResults);
    }
}
