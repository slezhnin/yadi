package com.lezhnin.junit.parameters.predicate.number;

import java.util.function.Predicate;

public class PositiveNumber implements Predicate<Number> {

    @Override
    public boolean test(final Number number) {
        return number.doubleValue() >= 0D;
    }

    @Override
    public String toString() {
        return "PositiveNumber()";
    }
}
