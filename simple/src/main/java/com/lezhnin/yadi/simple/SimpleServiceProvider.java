package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceConstructionException;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceProvider;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    private MethodDependency[] findMethodDependencies(final ServiceDependency[] dependencies) {
        List<MethodDependency> methodDependencies = new ArrayList<>(dependencies.length);
        for (ServiceDependency dependency : dependencies) {
            if (dependency instanceof MethodDependency) {
                methodDependencies.add((MethodDependency) dependency);
            }
        }
        return methodDependencies.toArray(new MethodDependency[methodDependencies.size()]);
    }

    @Nullable
    private ConstructorDependency findConstructorDependency(final ServiceDependency[] dependencies) {
        for (ServiceDependency dependency : dependencies) {
            if (dependency instanceof ConstructorDependency) {
                return (ConstructorDependency) dependency;
            }
        }
        return null;
    }

    @Nonnull
    public static <T> ServiceProvider<T> provider(@Nonnull final Class<T> implementation, @Nonnull final ServiceDependency... dependencies) {
        return new SimpleServiceProvider<>(implementation, dependencies);
    }

    @Nonnull
    @Override
    public T provide(@Nonnull final ServiceLocator serviceLocator) {
        try {
            final T instance = constructorDependency == null ?
                    implementation.newInstance() :
                    implementation.getConstructor(
                            constructorDependency.getDependencies()
                    ).newInstance(parameters(serviceLocator, constructorDependency.getDependencies()));
            for (MethodDependency methodDependency : methodDependencies) {
                methodDependency.getMethod().invoke(
                        instance,
                        parameters(serviceLocator, methodDependency.getDependencies())
                );
            }
            return instance;
        } catch (Exception e) {
            throw new ServiceConstructionException(implementation, e);
        }
    }

    private Object[] parameters(final ServiceLocator serviceLocator, final Class<?>[] dependencies) {
        final Object[] parameters = new Object[dependencies.length];
        for (int i = 0; i < dependencies.length; i++) {
            parameters[i] = serviceLocator.locate(dependencies[i]);
        }
        return parameters;
    }
}
