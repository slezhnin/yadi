package com.lezhnin.junit.parameters.supplier.random;

public class RandomDoubleSupplier extends RandomSupplier<Double> {

    @Override
    public Double get() {
        if (getRandom().nextBoolean()) {
            return getRandom().nextDouble();
        } else {
            return -getRandom().nextDouble();
        }
    }
}
