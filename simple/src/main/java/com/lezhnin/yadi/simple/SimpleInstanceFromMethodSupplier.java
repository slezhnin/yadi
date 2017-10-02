package com.lezhnin.yadi.simple;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceConstructionException;
import com.lezhnin.yadi.api.ServiceLocator;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleInstanceFromMethodSupplier<T> implements Supplier<T> {

    private final ServiceLocator serviceLocator;
    private final Class<T> implementation;
    private final Object instance;
    private final Method method;

    public SimpleInstanceFromMethodSupplier(@Nonnull ServiceLocator serviceLocator,
                                            @Nonnull Class<T> implementation,
                                            @Nonnull Method method,
                                            @Nullable Object instance) {
        this.serviceLocator = requireNonNull(serviceLocator);
        this.implementation = requireNonNull(implementation);
        this.method = requireNonNull(method);
        if (instance != null) {
            this.instance = instance;
        } else {
            try {
                this.instance = method.getDeclaringClass().newInstance();
            } catch (Exception e) {
                throw new ServiceConstructionException(method.getDeclaringClass(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get() {
        try {
            return (T) method.invoke(instance, parameters(serviceLocator, method.getParameterTypes()));
        } catch (Exception e) {
            throw new ServiceConstructionException(implementation, e);
        }
    }

    private Object[] parameters(final ServiceLocator serviceLocator, final Class<?>[] dependencies) {
        return stream(dependencies).map(serviceLocator::locate).map(Supplier::get).toArray();
    }
}
