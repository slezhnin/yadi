package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceProviderFinder;
import com.lezhnin.yadi.api.ServiceProvider;
import com.lezhnin.yadi.api.ServiceStorage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceProviderFinder implements ServiceProviderFinder {

    private final ServiceStorage serviceStorage;

    public SimpleServiceProviderFinder(@Nonnull ServiceStorage serviceStorage) {
        this.serviceStorage = requireNonNull(serviceStorage);
    }

    @Nullable
    @Override
    public <T> ServiceProvider<T> find(@Nonnull final Class<T> serviceBean) {
        return serviceStorage.get(serviceBean);
    }
}
