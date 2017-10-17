package com.lezhnin.junit.parameters;

import java.util.Random;
import java.util.function.Supplier;

public class BaseSupplier implements Supplier<Object> {

    private static final Random random = new Random();

    private final Supplier<?> supplier;
    private final Class<?> parameterType;
    private final int maxSize;

    BaseSupplier(final Supplier<?> supplier, final Class<?> parameterType, final int maxSize) {
        this.supplier = supplier;
        this.parameterType = parameterType;
        this.maxSize = maxSize;
    }

    static Random getRandom() {
        return random;
    }

    public Supplier<?> getSupplier() {
        return supplier;
    }

    Class<?> getParameterType() {
        return parameterType;
    }

    int getMaxSize() {
        return maxSize;
    }

    @Override
    public Object get() {
        return supplier.get();
    }
}
