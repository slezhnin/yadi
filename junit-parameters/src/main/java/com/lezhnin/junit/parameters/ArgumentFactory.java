package com.lezhnin.junit.parameters;

import java.util.Set;
import java.util.function.Supplier;

public class ArgumentFactory implements Supplier<Object> {

    private final Supplier<?> supplier;
    private final Class<?> parameterType;
    private final int maxSize;

    ArgumentFactory(final Class<? extends Supplier<?>> supplierType, final Class<?> parameterType, final int maxSize) {
        this.supplier = instance(supplierType);
        this.parameterType = parameterType;
        this.maxSize = maxSize;
    }

    private <T> T instance(final Class<T> type) {
        try {
            return type.newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
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
