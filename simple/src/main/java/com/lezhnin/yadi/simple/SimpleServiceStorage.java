package com.lezhnin.yadi.simple;

import com.lezhnin.yadi.api.ServiceProvider;
import com.lezhnin.yadi.api.ServiceStorage;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceStorage implements ServiceStorage {

    private Map<Class<?>, ServiceProvider<?>> storage = new LinkedHashMap<>();

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> ServiceProvider<T> get(@Nonnull final Class<T> serviceBeanInterface) {
        return (ServiceProvider<T>) storage.get(serviceBeanInterface);
    }

    @Nonnull
    @Override
    public <T> ServiceStorage put(@Nonnull final Class<? extends T> serviceBeanInterface,
                                  @Nonnull final ServiceProvider<? extends T> serviceProvider) {
        storage.put(serviceBeanInterface, serviceProvider);
        return this;
    }
}
