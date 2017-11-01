package com.lezhnin.junit.parameters.supplier;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class PredicateSupplier<T> implements Supplier<Predicate<T>> {

    private final Class<?> suppliedClass;
    private final String[] limits;

    public PredicateSupplier(final Class<?> suppliedClass, final String[] limits) {
        this.suppliedClass = suppliedClass;
        this.limits = limits;
    }

    @Override
    public Predicate<T> get() {
        if (Number.class.isAssignableFrom(suppliedClass)) {
            return new NumberPredicateSupplier<T>(limits).get();
        }
        return null;
    }
}
