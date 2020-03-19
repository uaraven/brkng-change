package net.ninjacat.brking.diff.rules.methods;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ApiObjectPool;

import java.util.List;
import java.util.function.Function;

public class MethodPool extends ApiObjectPool<ApiMethod> {

    protected MethodPool(final List<ApiMethod> objects, final Function<ApiMethod, String> nameExtractor) {
        super(objects, nameExtractor);
    }

    public static MethodPool of(final ApiClass cls) {
        return new MethodPool(cls.methods(), ApiMethod::identifier);
    }

}
