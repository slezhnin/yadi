package com.lezhnin.yadi.simple;

import com.lezhnin.yadi.api.ServiceBeanProvider;
import com.lezhnin.yadi.api.ServiceBeanStorage;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceBeanStorage implements ServiceBeanStorage {

    private Map<Class<?>, ServiceBeanProvider<?>> storage = new LinkedHashMap<>();

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> ServiceBeanProvider<T> get(@Nonnull final Class<T> serviceBeanInterface) {
        return (ServiceBeanProvider<T>) storage.get(serviceBeanInterface);
    }

    @Nonnull
    @Override
    public <T> ServiceBeanStorage put(@Nonnull final Class<? extends T> serviceBeanInterface,
                                      @Nonnull final ServiceBeanProvider<? extends T> serviceBeanProvider) {
        storage.put(serviceBeanInterface, serviceBeanProvider);
        return this;
    }
}
