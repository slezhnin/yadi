package com.lezhnin.junit.parameters.supplier.random;

public class RandomDoubleSupplier extends RandomSupplier<Double> {

    @Override
    public Double get() {
        return getRandom().nextDouble();
    }
}
