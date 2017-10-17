package com.lezhnin.junit.parameters.factory;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.generate;
import java.util.function.Supplier;

public class ListSupplier extends BaseSupplier {

    ListSupplier(final Supplier<?> supplier, final Class<?> parameterType, final int maxSize) {
        super(supplier, parameterType, maxSize);
    }

    @Override
    public Object get() {
        return generate(getSupplier())
                .limit(getRandom().nextInt(getMaxSize()))
                .collect(toList());
    }
}
