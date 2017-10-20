package com.lezhnin.junit.parameters.provider;

import java.util.function.Supplier;

public class ProviderFromSupplier<T> implements ValueProvider<T> {

    private final Supplier<T> supplier;
    private final Class<?> suppliedClass;

    public ProviderFromSupplier(final Supplier<T> supplier, final Class<?> suppliedClass) {
        this.supplier = supplier;
        this.suppliedClass = suppliedClass;
    }

    @Override
    public T apply(final Class<?> actualClass) {
        return checkTypeAndReturn(actualClass, supplier.get());
    }

    @Override
    public Class<?> getProvidedClass() {
        return suppliedClass;
    }
}
