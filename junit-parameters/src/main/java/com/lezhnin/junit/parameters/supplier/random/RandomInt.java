package com.lezhnin.junit.parameters.supplier.random;

import java.util.Random;
import java.util.function.Supplier;

public class RandomInt implements Supplier<Integer> {

    private static final Random random = new Random();

    @Override
    public Integer get() {
        return random.nextInt();
    }
}
