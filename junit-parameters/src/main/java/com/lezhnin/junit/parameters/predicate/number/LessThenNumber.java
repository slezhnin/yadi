package com.lezhnin.junit.parameters.predicate.number;

import java.util.function.Predicate;

public class LessThenNumber implements Predicate<Number> {

    private final double limit;

    public LessThenNumber(final double limit) {
        this.limit = limit;
    }

    @Override
    public boolean test(final Number number) {
        return number.doubleValue() < limit;
    }
}
