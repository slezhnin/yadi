package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceConstructionException;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceProvider;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceProvider<T> implements ServiceProvider<T> {

    private final Class<T> implementation;
    private final Class<?>[] dependencies;

    private SimpleServiceProvider(@Nonnull final Class<T> implementation, @Nonnull final Class<?>... dependencies) {
        this.implementation = requireNonNull(implementation);
        this.dependencies = dependencies;
        for (Class<?> dependency : dependencies) {
            requireNonNull(dependency);
        }
    }

    @Nonnull
    public static <T> ServiceProvider<T> provider(@Nonnull final Class<T> implementation, @Nonnull final Class<?>... dependencies) {
        return new SimpleServiceProvider<>(implementation, dependencies);
    }

    @Nullable
    @Override
    public T provide(@Nonnull final ServiceLocator serviceLocator) {
        try {
            if (dependencies.length == 0 || implementation.getConstructors().length == 0) {
                return implementation.newInstance();
            }
            final Object[] parameters = new Object[dependencies.length];
            for (int i = 0; i < dependencies.length; i++) {
                parameters[i] = serviceLocator.locate(dependencies[i]);
            }
            return implementation.getConstructor(dependencies).newInstance(parameters);
        } catch (Exception e) {
            throw new ServiceConstructionException(implementation, e);
        }
    }
}
