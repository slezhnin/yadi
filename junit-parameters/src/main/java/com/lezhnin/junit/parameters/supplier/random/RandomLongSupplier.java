package com.lezhnin.junit.parameters.supplier.random;

public class RandomLongSupplier extends RandomSupplier<Long> {

    @Override
    public Long get() {
        return getRandom().nextLong();
    }
}
