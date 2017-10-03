package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceSupplier;
import com.lezhnin.yadi.api.ServiceFinder;
import com.lezhnin.yadi.api.ServiceStorage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceFinder implements ServiceFinder {

    private final ServiceStorage serviceStorage;

    public SimpleServiceFinder(@Nonnull ServiceStorage serviceStorage) {
        this.serviceStorage = requireNonNull(serviceStorage);
    }

    @Nullable
    @Override
    public <T> ServiceSupplier<T> find(@Nonnull final String serviceId) {
        return serviceStorage.get(serviceId);
    }

    @Nullable
    @Override
    public <T> ServiceSupplier<T> find(@Nonnull final Class<T> serviceInterface) {
        return serviceStorage.get(serviceInterface.getName());
    }
}
