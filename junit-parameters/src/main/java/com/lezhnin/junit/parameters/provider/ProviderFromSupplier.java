package com.lezhnin.junit.parameters.provider;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.generate;
import com.lezhnin.junit.parameters.exception.LimitsUnreachableException;
import com.lezhnin.junit.parameters.supplier.PredicateSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ProviderFromSupplier<T> implements ValueProvider<T> {

    private final Supplier<T> supplier;
    private final Class<?> suppliedClass;
    private Predicate<T> predicate = null;

    public ProviderFromSupplier(final Supplier<T> supplier, final Class<?> suppliedClass) {
        this.supplier = requireNonNull(supplier);
        this.suppliedClass = requireNonNull(suppliedClass);
    }

    private T limitedValue() {
        return generate(supplier)
                .limit(1000000)
                .filter(value -> predicate == null || predicate.test(value))
                .findFirst()
                .orElseThrow(() -> new LimitsUnreachableException(suppliedClass, predicate));
    }

    @Override
    public T apply(final Class<?> actualClass) {
        return checkTypeAndReturn(actualClass, limitedValue());
    }

    @Override
    public Class<?> getProvidedClass() {
        return suppliedClass;
    }

    @Override
    public void setLimits(final String[] limits) {
        predicate = new PredicateSupplier<T>(suppliedClass, limits).get();
    }
}
