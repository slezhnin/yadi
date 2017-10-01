package com.lezhnin.yadi.simple;

import com.lezhnin.yadi.api.ServiceReference;

import javax.annotation.Nonnull;

import static com.lezhnin.yadi.simple.SimpleServiceReference.immediate;

public final class SimpleImmediateValue {

    private SimpleImmediateValue() {
    }

    @Nonnull
    public static ServiceReference<String> immediateValue(@Nonnull String value) {
        return immediate(String.class, value);
    }

    @Nonnull
    public static ServiceReference<Integer> immediateValue(@Nonnull Integer value) {
        return immediate(Integer.class, value);
    }

    @Nonnull
    public static ServiceReference<Long> immediateValue(@Nonnull Long value) {
        return immediate(Long.class, value);
    }

    @Nonnull
    public static ServiceReference<Double> immediateValue(@Nonnull Double value) {
        return immediate(Double.class, value);
    }
}
