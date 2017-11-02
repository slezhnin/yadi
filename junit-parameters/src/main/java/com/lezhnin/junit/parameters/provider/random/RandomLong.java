package com.lezhnin.junit.parameters.provider.random;

import com.lezhnin.junit.parameters.provider.ProviderFromSupplier;
import com.lezhnin.junit.parameters.supplier.random.RandomLongSupplier;

public class RandomLong extends ProviderFromSupplier<Long> {

    public RandomLong() {
        super(new RandomLongSupplier(), Long.class);
    }
}
