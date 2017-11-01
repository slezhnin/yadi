package com.lezhnin.junit.parameters.supplier;

import com.lezhnin.junit.parameters.provider.ValueProvider;
import java.lang.reflect.Array;

public class ArraySupplier<T> extends BaseSupplier<T, Object> {

    ArraySupplier(final ValueProvider<T> provider, final int maxSize) {
        super(provider, provider.getProvidedClass(), maxSize);
    }

    @Override
    public Object get() {
        return generateFromProvider().toArray(length -> (Object[]) Array.newInstance(getParameterType(), length));
    }
}
