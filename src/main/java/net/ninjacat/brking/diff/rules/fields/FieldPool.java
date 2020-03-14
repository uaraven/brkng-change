package net.ninjacat.brking.diff.rules.fields;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiObjectPool;

import java.util.List;
import java.util.function.Function;

public class FieldPool extends ApiObjectPool<ApiField> {

    protected FieldPool(final List<ApiField> objects, final Function<ApiField, String> nameExtractor) {
        super(objects, nameExtractor);
    }

    public static FieldPool of(final ApiClass cls) {
        return new FieldPool(cls.fields(), ApiField::name);
    }

}
