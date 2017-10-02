package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceBinder;
import com.lezhnin.yadi.api.ServiceImplementor;
import com.lezhnin.yadi.api.ServiceProvider;
import com.lezhnin.yadi.api.ServiceStorage;
import javax.annotation.Nonnull;

public class SimpleServiceBinder implements ServiceBinder {

    private final ServiceStorage serviceStorage;

    public SimpleServiceBinder(@Nonnull final ServiceStorage serviceStorage) {
        this.serviceStorage = requireNonNull(serviceStorage);
    }

    @Nonnull
    @Override
    public <T> ServiceImplementor<T> bind(@Nonnull final Class<T> serviceInterface) {
        return bind(serviceInterface.getName());
    }

    @Nonnull
    @Override
    public <T> ServiceImplementor<T> bind(@Nonnull final String serviceId) {
        return new ServiceImplementor<T>() {
            @Nonnull
            @Override
            public ServiceBinder to(@Nonnull final ServiceProvider<? extends T> serviceProvider) {
                serviceStorage.put(requireNonNull(serviceId), requireNonNull(serviceProvider));
                return SimpleServiceBinder.this;
            }
        };
    }
}
