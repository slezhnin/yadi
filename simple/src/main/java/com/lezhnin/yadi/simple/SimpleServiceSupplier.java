package com.lezhnin.yadi.simple;

import com.lezhnin.yadi.api.ServiceConstructionException;
import com.lezhnin.yadi.api.ServiceLocator;

import java.util.function.Supplier;

import static java.util.Arrays.stream;

public class SimpleServiceSupplier<T> implements Supplier<T> {

    private final ServiceLocator serviceLocator;
    private final Supplier<T> instanceSupplier;
    private final MethodDependency[] methodDependencies;

    public SimpleServiceSupplier(ServiceLocator serviceLocator, Supplier<T> instanceSupplier, MethodDependency[] methodDependencies) {
        this.serviceLocator = serviceLocator;
        this.instanceSupplier = instanceSupplier;
        this.methodDependencies = methodDependencies;
    }

    @Override
    public T get() {
        final T instance = instanceSupplier.get();
        stream(methodDependencies).forEach(md -> {
            try {
                md.getMethod().invoke(instance, parameters(serviceLocator, md.getDependencies()));
            } catch (Exception e) {
                throw new ServiceConstructionException(instance.getClass(), e);
            }
        });
        return instance;
    }

    private Object[] parameters(final ServiceLocator serviceLocator, final Class<?>[] dependencies) {
        return stream(dependencies).map(serviceLocator::locate).map(Supplier::get).toArray();
    }
}
