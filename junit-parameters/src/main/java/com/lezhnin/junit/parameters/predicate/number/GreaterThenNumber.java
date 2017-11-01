package com.lezhnin.junit.parameters.predicate.number;

import java.util.function.Predicate;

public class GreaterThenNumber implements Predicate<Number> {

    private final double limit;

    public GreaterThenNumber(final double limit) {
        this.limit = limit;
    }

    @Override
    public boolean test(final Number number) {
        return number.doubleValue() > limit;
    }

    @Override
    public String toString() {
        return "GreaterThenNumber(" + limit + ")";
    }
}
