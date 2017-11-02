package com.lezhnin.junit.parameters.supplier;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import com.lezhnin.junit.parameters.predicate.number.GreaterThenNumber;
import com.lezhnin.junit.parameters.predicate.number.GreaterThenOrEqualsNumber;
import com.lezhnin.junit.parameters.predicate.number.LessThenNumber;
import com.lezhnin.junit.parameters.predicate.number.LessThenOrEqualsNumber;
import com.lezhnin.junit.parameters.predicate.number.NegativeNumber;
import com.lezhnin.junit.parameters.predicate.number.PositiveNumber;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NumberPredicateSupplier<T> implements Supplier<Predicate<T>> {

    private final String[] limits;

    public NumberPredicateSupplier(final String[] limits) {
        this.limits = limits;
    }

    @Override
    public Predicate<T> get() {
        return stream(limits)
                .map(this::fromLimit)
                .filter(Objects::nonNull)
                .reduce(Predicate::and)
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    Predicate<T> fromLimit(final String limit) {
        requireNonNull(limit);
        if ("negative".equals(limit)) {
            return (Predicate<T>) new NegativeNumber();
        }
        if ("positive".equals(limit)) {
            return (Predicate<T>) new PositiveNumber();
        }
        if (limit.startsWith(">=")) {
            return (Predicate<T>) new GreaterThenOrEqualsNumber(getLimit(limit, 2));
        }
        if (limit.startsWith("<=")) {
            return (Predicate<T>) new LessThenOrEqualsNumber(getLimit(limit, 2));
        }
        if (limit.startsWith(">")) {
            return (Predicate<T>) new GreaterThenNumber(getLimit(limit, 1));
        }
        if (limit.startsWith("<")) {
            return (Predicate<T>) new LessThenNumber(getLimit(limit, 1));
        }
        return null;
    }

    private Double getLimit(final String limit, final int skip) {
        return Double.valueOf(limit.substring(skip).trim());
    }
}
