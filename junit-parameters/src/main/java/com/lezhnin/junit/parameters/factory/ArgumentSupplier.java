package com.lezhnin.junit.parameters.factory;

import static java.util.Objects.requireNonNull;
import com.lezhnin.junit.parameters.provider.ValueProvider;
import java.util.Set;
import java.util.function.Supplier;

public class ArgumentSupplier<T> implements Supplier<Object> {

    private final ValueProvider<T> provider;
    private final Class<?> parameterType;
    private final int maxSize;

    ArgumentSupplier(final ValueProvider<T> provider, final Class<?> parameterType, final int maxSize) {
        this.provider = requireNonNull(provider);
        this.parameterType = requireNonNull(parameterType);
        this.maxSize = maxSize;
    }

    @Override
    public Object get() {
        return getSupplier().get();
    }

    private Supplier<Object> getSupplier() {
        if (parameterType.getName().startsWith("[L") && parameterType.getName().endsWith(";")) {
            return new ArraySupplier<>(provider, maxSize);
        } else if (Set.class.isAssignableFrom(parameterType)) {
            return new SetSupplier<>(provider, maxSize);
        } else if (Iterable.class.isAssignableFrom(parameterType)) {
            return new ListSupplier<>(provider, maxSize);
        } else {
            return () -> provider.apply(parameterType);
        }
    }
}
