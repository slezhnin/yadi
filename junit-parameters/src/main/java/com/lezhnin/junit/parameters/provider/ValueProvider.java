package com.lezhnin.junit.parameters.provider;

import com.lezhnin.junit.parameters.exception.TypeMismatchException;
import java.util.function.Function;

public interface ValueProvider<T> extends Function<Class<?>, T> {

    Class<?> getProvidedClass();

    default T checkTypeAndReturn(final Class<?> actual, final T value) {
        if (!getProvidedClass().isAssignableFrom(actual)) {
            throw new TypeMismatchException(getProvidedClass(), actual);
        }
        return value;
    }

    void setLimits(final String[] limits);
}
