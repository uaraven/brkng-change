package net.ninjacat.brking.diff.rules;

import com.google.common.collect.ImmutableList;
import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.rules.methods.*;

import java.util.ArrayList;
import java.util.List;

public class ClassMethodsRule implements ClassDiffRule {

    private static final List<MethodDiffRule> methodRules = List.of(
            new MethodVisibilityChanged(),
            new MethodModifiersChanged(),
            new MethodReturnTypeChanged(),
            new MethodRemoved(),
            new MethodExceptionsChanged()
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
