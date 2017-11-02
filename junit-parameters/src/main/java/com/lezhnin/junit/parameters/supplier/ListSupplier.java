package com.lezhnin.junit.parameters.supplier;

import static java.util.stream.Collectors.toList;
import com.lezhnin.junit.parameters.provider.ValueProvider;

public class ListSupplier<T> extends BaseSupplier<T, Object> {

    ListSupplier(final ValueProvider<T> provider, final int maxSize) {
        super(provider, provider.getProvidedClass(), maxSize);
    }

    @Override
    public Object get() {
        return generateFromProvider().collect(toList());
    }
}
