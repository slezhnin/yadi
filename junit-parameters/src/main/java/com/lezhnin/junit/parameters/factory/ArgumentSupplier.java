package com.lezhnin.junit.parameters.factory;

import java.util.Set;
import java.util.function.Supplier;

public class ArgumentSupplier implements Supplier<Object> {

    private final Supplier<?> supplier;
    private final Class<?> parameterType;
    private final int maxSize;

    ArgumentSupplier(final Supplier<?> supplier, final Class<?> parameterType, final int maxSize) {
        this.supplier = supplier;
        this.parameterType = parameterType;
        this.maxSize = maxSize;
    }

    @Override
    public Object get() {
        return getSupplier().get();
    }

    private Supplier<?> getSupplier() {
        if (parameterType.getName().startsWith("[L") && parameterType.getName().endsWith(";")) {
            return new ArraySupplier(supplier, parameterType, maxSize);
        } else if (parameterType.isAssignableFrom(Set.class)) {
            return new SetSupplier(supplier, parameterType, maxSize);
        } else if (parameterType.isAssignableFrom(Iterable.class)) {
            return new ListSupplier(supplier, parameterType, maxSize);
        } else {
            return supplier;
        }
    }
}
