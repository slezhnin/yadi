package com.lezhnin.junit.parameters.provider.random;

import com.lezhnin.junit.parameters.provider.ProviderFromSupplier;
import com.lezhnin.junit.parameters.supplier.random.RandomIntSupplier;

public class RandomInt extends ProviderFromSupplier<Integer> {

    public RandomInt() {
        super(new RandomIntSupplier(), Integer.class);
    }
}
