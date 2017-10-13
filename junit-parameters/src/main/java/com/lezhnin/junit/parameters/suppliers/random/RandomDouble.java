package com.lezhnin.junit.parameters.suppliers.random;

import java.util.Random;
import java.util.function.Supplier;

public class RandomDouble implements Supplier<Double> {

    private static final Random random = new Random();

    @Override
    public Double get() {
        return random.nextDouble();
    }
}
