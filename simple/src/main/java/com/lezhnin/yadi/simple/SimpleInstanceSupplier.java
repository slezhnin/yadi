package com.lezhnin.yadi.simple;

import com.lezhnin.yadi.api.ServiceConstructionException;
import com.lezhnin.yadi.api.ServiceLocator;

import java.util.function.Supplier;

import static java.util.Arrays.stream;

public class SimpleInstanceSupplier<T> implements Supplier<T> {

    private final ServiceLocator serviceLocator;
    private final Class<T> implementation;
    private final Class<?>[] dependencies;

    public SimpleInstanceSupplier(ServiceLocator serviceLocator, Class<T> implementation, Class<?>[] dependencies) {
        this.serviceLocator = serviceLocator;
        this.implementation = implementation;
        this.dependencies = dependencies;
    }

    @Override
    public T get() {
        try {
            return implementation.getConstructor(dependencies)
                    .newInstance(parameters(serviceLocator, dependencies));
        } catch (Exception e) {
            throw new ServiceConstructionException(implementation, e);
        }
    }

    private Object[] parameters(final ServiceLocator serviceLocator, final Class<?>[] dependencies) {
        return stream(dependencies).map(serviceLocator::locate).map(Supplier::get).toArray();
    }
}
