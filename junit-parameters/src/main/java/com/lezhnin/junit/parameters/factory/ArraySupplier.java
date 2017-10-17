package com.lezhnin.junit.parameters.factory;

import static java.util.stream.Stream.generate;
import java.lang.reflect.Array;
import java.util.function.Supplier;

public class ArraySupplier extends BaseSupplier {

    ArraySupplier(final Supplier<?> supplier, final Class<?> parameterType, final int maxSize) {
        super(supplier, parameterType, maxSize);
    }

    @Override
    public Object get() {
        final String typeName = getParameterType().getName();
        final Class<?> elementType = load(typeName.substring(2, typeName.length() - 1));
        return generate(getSupplier()).limit(getRandom().nextInt(getMaxSize()))
                                      .toArray(length -> (Object[]) Array.newInstance(elementType, length));
    }

    private Class<?> load(final String className) {
        try {
            return getParameterType().getClassLoader().loadClass(className);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
