package com.lezhnin.junit.parameters.predicate.number;

import java.util.function.Predicate;

public class GreaterThenOrEqualsNumber implements Predicate<Number> {

    private final double limit;

    public GreaterThenOrEqualsNumber(final double limit) {
        this.limit = limit;
    }

    @Override
    public boolean test(final Number number) {
        return number.doubleValue() >= limit;
    }
}
