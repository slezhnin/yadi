package com.lezhnin.yadi.simple;

import static com.lezhnin.yadi.simple.ConstructorDependency.constructor;
import static com.lezhnin.yadi.simple.MethodDependency.method;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceProvider;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceProvider<T> implements ServiceProvider<T> {

    private final Class<T> implementation;
    private final MethodDependency[] methodDependencies;
    private final ServiceDependency constructorDependency;

    private SimpleServiceProvider(@Nonnull final Class<T> implementation,
                                  @Nonnull final ServiceDependency... dependencies) {
        this.implementation = requireNonNull(implementation);
        for (ServiceDependency dependency : dependencies) {
            requireNonNull(dependency);
        }
        constructorDependency = findConstructorDependency(dependencies);
        methodDependencies = findMethodDependencies(dependencies);
    }

    private SimpleServiceProvider(@Nonnull final Class<T> implementation,
                                  @Nullable Object instance,
                                  @Nonnull final Method method,
                                  @Nonnull final ServiceDependency... dependencies) {
        this.implementation = requireNonNull(implementation);
        for (ServiceDependency dependency : dependencies) {
            requireNonNull(dependency);
        }
        constructorDependency = method(method, instance);
        methodDependencies = findMethodDependencies(dependencies);
    }

    @Nonnull
    public static <T> ServiceProvider<T> provider(@Nonnull final Class<T> implementation,
                                                  @Nonnull final ServiceDependency... dependencies) {
        return new SimpleServiceProvider<>(implementation, dependencies);
    }

    @Nonnull
    public static <T> ServiceProvider<T> provider(@Nonnull final Class<T> implementation,
                                                  @Nullable Object instance,
                                                  @Nonnull final Method method,
                                                  @Nonnull final ServiceDependency... dependencies) {
        return new SimpleServiceProvider<>(implementation, instance, method, dependencies);
    }

    @Nonnull
    @Override
    public Supplier<T> apply(@Nonnull final ServiceLocator serviceLocator) {
        return runMethods(getInstance(serviceLocator), serviceLocator);
    }

    private Supplier<T> getInstance(final @Nonnull ServiceLocator serviceLocator) {
        if (constructorDependency instanceof ConstructorDependency) {
            return new SimpleInstanceSupplier<>(serviceLocator, implementation, constructorDependency.getDependencies());
        }
        return new SimpleInstanceFromMethodSupplier<>(
                serviceLocator,
                implementation,
                ((MethodDependency) constructorDependency).getMethod(),
                ((MethodDependency) constructorDependency).getInstance()
        );
    }

    private Supplier<T> runMethods(Supplier<T> instanceSupplier, final ServiceLocator serviceLocator) {
        return new SimpleServiceSupplier<>(serviceLocator, instanceSupplier, methodDependencies);
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
}
