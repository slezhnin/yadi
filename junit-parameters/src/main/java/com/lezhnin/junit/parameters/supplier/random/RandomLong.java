package com.lezhnin.junit.parameters.supplier.random;

import java.util.Random;
import java.util.function.Supplier;

public class RandomLong implements Supplier<Long> {

    private static final Random random = new Random();

    @Override
    public Long get() {
        return random.nextLong();
    }
}
