package com.lezhnin.junit.parameters.suppliers.random;

import java.util.UUID;
import java.util.function.Supplier;

public class RandomString implements Supplier<String> {

    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}
