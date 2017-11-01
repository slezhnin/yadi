package com.lezhnin.junit.parameters.supplier;

import static java.util.stream.Collectors.toSet;
import com.lezhnin.junit.parameters.provider.ValueProvider;

public class SetSupplier<T> extends BaseSupplier<T, Object> {

    SetSupplier(final ValueProvider<T> provider, final int maxSize) {
        super(provider, provider.getProvidedClass(), maxSize);
    }

    @Override
    public Object get() {
        return generateFromProvider().collect(toSet());
    }
}
