package com.lezhnin.junit.parameters.supplier.random;

import java.util.Random;
import java.util.function.Supplier;

public abstract class RandomSupplier<T> implements Supplier<T> {

    private static final Random random = new Random();

    public static Random getRandom() {
        return random;
    }
}
