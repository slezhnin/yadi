package com.lezhnin.junit.parameters.provider;

import static org.mockito.Mockito.mock;

public class Mock implements ValueProvider<Object> {

    @Override
    public Object apply(final Class<?> aClass) {
        return mock(aClass);
    }

    @Override
    public Class<?> getProvidedClass() {
        return Object.class;
    }
}
