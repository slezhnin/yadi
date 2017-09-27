package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceBeanConstructionException;
import com.lezhnin.yadi.api.ServiceBeanLocator;
import com.lezhnin.yadi.api.ServiceBeanProvider;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceBeanProvider<T> implements ServiceBeanProvider<T> {

    private final Class<T> implementation;
    private final Class<?>[] dependencies;

    private SimpleServiceBeanProvider(@Nonnull final Class<T> implementation, @Nonnull final Class<?>... dependencies) {
        this.implementation = requireNonNull(implementation);
        this.dependencies = dependencies;
        for (Class<?> dependency : dependencies) {
            requireNonNull(dependency);
        }
    }

    @Nonnull
    public static <T> ServiceBeanProvider<T> provider(@Nonnull final Class<T> implementation,@Nonnull final Class<?>... dependencies) {
        return new SimpleServiceBeanProvider<>(implementation, dependencies);
    }

    @Nullable
    @Override
    public T provide(@Nonnull final ServiceBeanLocator serviceBeanLocator) {
        try {
            if (dependencies.length == 0 || implementation.getConstructors().length == 0) {
                return implementation.newInstance();
            }
            final Object[] parameters = new Object[dependencies.length];
            for (int i = 0; i < dependencies.length; i++) {
                parameters[i] = serviceBeanLocator.locate(dependencies[i]);
            }
            return implementation.getConstructor(dependencies).newInstance(parameters);
        } catch (Exception e) {
            throw new ServiceBeanConstructionException(implementation, e);
        }
    }
}
