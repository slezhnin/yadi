package com.lezhnin.junit.parameters.provider.random;

import com.lezhnin.junit.parameters.provider.ProviderFromSupplier;
import com.lezhnin.junit.parameters.supplier.random.RandomDoubleSupplier;

public class RandomDouble extends ProviderFromSupplier<Double> {

    public RandomDouble() {
        super(new RandomDoubleSupplier(), Double.class);
    }
}
