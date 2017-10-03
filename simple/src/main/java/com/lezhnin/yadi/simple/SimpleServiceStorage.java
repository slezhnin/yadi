package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceSupplier;
import com.lezhnin.yadi.api.ServiceStorage;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceStorage implements ServiceStorage {

    private Map<String, ServiceSupplier<?>> storage = new LinkedHashMap<>();

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> ServiceSupplier<T> get(@Nonnull final String serviceId) {
        return (ServiceSupplier<T>) storage.get(serviceId);
    }

    @Nonnull
    @Override
    public <T> ServiceStorage put(@Nonnull final String serviceId,
                                  @Nonnull final ServiceSupplier<? extends T> serviceSupplier) {
        storage.put(requireNonNull(serviceId), requireNonNull(serviceSupplier));
        return this;
    }
}
