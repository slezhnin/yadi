package com.lezhnin.junit.parameters.supplier.random;

import static java.util.UUID.randomUUID;
import java.util.function.Supplier;

public class RandomUUIDString implements Supplier<String> {

    @Override
    public String get() {
        return randomUUID().toString();
    }
}
