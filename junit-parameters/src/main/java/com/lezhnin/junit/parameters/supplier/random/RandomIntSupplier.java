package com.lezhnin.junit.parameters.supplier.random;

public class RandomIntSupplier extends RandomSupplier<Integer> {

    @Override
    public Integer get() {
        return getRandom().nextInt();
    }
}
