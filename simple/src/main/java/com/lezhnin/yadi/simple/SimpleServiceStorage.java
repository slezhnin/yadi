package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceProvider;
import com.lezhnin.yadi.api.ServiceStorage;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceStorage implements ServiceStorage {

    private Map<String, ServiceProvider<?>> storage = new LinkedHashMap<>();

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> ServiceProvider<T> get(@Nonnull final String serviceId) {
        return (ServiceProvider<T>) storage.get(serviceId);
    }

    @Nonnull
    @Override
    public <T> ServiceStorage put(@Nonnull final String serviceId,
                                  @Nonnull final ServiceProvider<? extends T> serviceProvider) {
        storage.put(requireNonNull(serviceId), requireNonNull(serviceProvider));
        return this;
    }
}
