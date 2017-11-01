package com.lezhnin.junit.parameters.supplier;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import com.lezhnin.junit.parameters.Limit;
import com.lezhnin.junit.parameters.provider.ValueProvider;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.function.Supplier;

public class ArgumentSupplier<T> implements Supplier<Object> {

    private final ValueProvider<T> provider;
    private final Parameter parameter;
    private final int maxSize;

    public ArgumentSupplier(final ValueProvider<T> provider, final Parameter parameter, final int maxSize) {
        this.provider = requireNonNull(provider);
        this.parameter = requireNonNull(parameter);
        this.maxSize = maxSize;
        ofNullable(parameter.getAnnotation(Limit.class)).ifPresent(limits -> provider.setLimits(limits.value()));
    }

    @Override
    public Object get() {
        return getSupplier().get();
    }

    private Supplier<Object> getSupplier() {
        if (parameter.getType().getName().startsWith("[L") && parameter.getType().getName().endsWith(";")) {
            return new ArraySupplier<>(provider, maxSize);
        } else if (Set.class.isAssignableFrom(parameter.getType())) {
            return new SetSupplier<>(provider, maxSize);
        } else if (Iterable.class.isAssignableFrom(parameter.getType())) {
            return new ListSupplier<>(provider, maxSize);
        } else {
            return () -> provider.apply(parameter.getType());
        }
    }
}
