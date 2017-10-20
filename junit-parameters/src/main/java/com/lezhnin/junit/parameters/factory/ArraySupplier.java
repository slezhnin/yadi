package com.lezhnin.junit.parameters.factory;

import com.lezhnin.junit.parameters.provider.ValueProvider;
import java.lang.reflect.Array;

public class ArraySupplier<T> extends BaseSupplier<T, Object> {

    ArraySupplier(final ValueProvider<T> provider, final int maxSize) {
        super(provider, provider.getProvidedClass(), maxSize);
        //        super(provider, arrayElementType(parameterType), maxSize);
    }

    @Override
    public Object get() {
        return generateFromProvider().toArray(length -> (Object[]) Array.newInstance(getParameterType(), length));
    }

    //    private static Class<?> arrayElementType(final Class<?> parameterType) {
    //        try {
    //            final String typeName = parameterType.getName();
    //            final String className = typeName.substring(2, typeName.length() - 1);
    //            return parameterType.getClassLoader().loadClass(className);
    //        } catch (final Exception e) {
    //            throw new RuntimeException(e);
    //        }
    //    }
}
