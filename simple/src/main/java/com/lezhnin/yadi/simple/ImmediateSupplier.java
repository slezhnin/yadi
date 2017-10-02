package com.lezhnin.yadi.simple;

import java.util.function.Supplier;

public class ImmediateSupplier<T> implements Supplier<T> {

    private final T value;

    public ImmediateSupplier(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}
