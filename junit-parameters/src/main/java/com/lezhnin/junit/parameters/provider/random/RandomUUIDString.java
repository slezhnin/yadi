package com.lezhnin.junit.parameters.provider.random;

import com.lezhnin.junit.parameters.provider.ProviderFromSupplier;
import com.lezhnin.junit.parameters.supplier.random.RandomUUIDStringSupplier;

public class RandomUUIDString extends ProviderFromSupplier<String> {

    public RandomUUIDString() {
        super(new RandomUUIDStringSupplier(), String.class);
    }
}
