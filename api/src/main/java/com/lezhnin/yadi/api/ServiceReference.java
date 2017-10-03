package com.lezhnin.yadi.api;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceReference<T> {

    @Nonnull
    String getId();

    @Nonnull
    Class<T> getType();

    static <T> ServiceReference<T> serviceReference(@Nonnull final Class<T> type, @Nullable final String id) {
        return new ServiceReference<T>() {
            @Nonnull
            @Override
            public String getId() {
                return ofNullable(id).orElse(type.getName());
            }

            @Nonnull
            @Override
            public Class<T> getType() {
                return type;
            }
        };
    }

    static <T> ServiceReference<T> serviceReference(@Nonnull final Class<T> type) {
        return serviceReference(type, null);
    }

    @Nonnull
    static ServiceReference<?>[] fromTypes(@Nonnull final Class<?>... types) {
        final ServiceReference<?>[] references = new ServiceReference<?>[requireNonNull(types).length];
        for (int i = 0; i < types.length; i++) {
            references[i] = serviceReference(types[i]);
        }
        return references;
    }

    @Nonnull
    static Class<?>[] toTypes(@Nonnull final ServiceReference<?>... references) {
        return stream(requireNonNull(references)).map(ServiceReference::getType).toArray(Class<?>[]::new);
    }
}
