package com.lezhnin.yadi.api;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceBinder {

    interface ToSupplier<T> {

        @Nonnull
        ServiceBinder to(@Nonnull ServiceSupplier<T> serviceSupplier);
    }

    @Nonnull
    <T> ToSupplier<T> bind(@Nonnull Class<T> serviceType, @Nullable String serviceId);

    static ServiceBinder binder(@Nonnull final ServiceStorage serviceStorage) {
        class DefaultServiceBinder implements ServiceBinder {

            @Nonnull
            @Override
            public <T> ToSupplier<T> bind(@Nonnull final Class<T> serviceType, @Nullable final String serviceId) {
                return new ToSupplier<T>() {
                    @Nonnull
                    @Override
                    public ServiceBinder to(@Nonnull final ServiceSupplier<T> serviceSupplier) {
                        requireNonNull(serviceStorage).put(
                                ofNullable(serviceId).orElse(serviceType.getName()),
                                requireNonNull(serviceSupplier));
                        return DefaultServiceBinder.this;
                    }
                };
            }
        }
        return new DefaultServiceBinder();
    }
}
