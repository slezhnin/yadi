package com.lezhnin.yadi.simple;

import static com.lezhnin.yadi.simple.ConstructorDependency.constructor;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceConstructionException;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceProvider;
import javax.annotation.Nonnull;

public class SimpleServiceProvider<T> implements ServiceProvider<T> {

    private final Class<T> implementation;
    private final MethodDependency[] methodDependencies;
    private final ConstructorDependency constructorDependency;

    private SimpleServiceProvider(@Nonnull final Class<T> implementation, @Nonnull final ServiceDependency... dependencies) {
        this.implementation = requireNonNull(implementation);
        for (ServiceDependency dependency : dependencies) {
            requireNonNull(dependency);
        }
        constructorDependency = findConstructorDependency(dependencies);
        methodDependencies = findMethodDependencies(dependencies);
    }

    @Nonnull
    public static <T> ServiceProvider<T> provider(@Nonnull final Class<T> implementation, @Nonnull final ServiceDependency... dependencies) {
        return new SimpleServiceProvider<>(implementation, dependencies);
    }

    @Nonnull
    @Override
    public T provide(@Nonnull final ServiceLocator serviceLocator) {
        return runMethods(getInstance(serviceLocator), serviceLocator);
    }

    private T getInstance(final @Nonnull ServiceLocator serviceLocator) {
        try {
            return implementation.getConstructor(constructorDependency.getDependencies())
                                 .newInstance(parameters(serviceLocator, constructorDependency.getDependencies()));
        } catch (Exception e) {
            throw new ServiceConstructionException(implementation, e);
        }
    }

    private T runMethods(T instance, final ServiceLocator serviceLocator) {
        stream(methodDependencies).forEach(md -> {
            try {
                md.getMethod().invoke(instance, parameters(serviceLocator, md.getDependencies()));
            } catch (Exception e) {
                throw new ServiceConstructionException(implementation, e);
            }
        });
        return instance;
    }

    private MethodDependency[] findMethodDependencies(final ServiceDependency[] dependencies) {
        return stream(dependencies).filter(d -> d instanceof MethodDependency).toArray(MethodDependency[]::new);
    }

    @Nonnull
    private ConstructorDependency findConstructorDependency(final ServiceDependency[] dependencies) {
        return (ConstructorDependency) stream(dependencies)
                .filter(d -> d instanceof ConstructorDependency)
                .findFirst().orElse(constructor());
    }

    private Object[] parameters(final ServiceLocator serviceLocator, final Class<?>[] dependencies) {
        return stream(dependencies).map(serviceLocator::locate).toArray();
    }
}
