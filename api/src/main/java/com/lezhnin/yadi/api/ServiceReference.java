package com.lezhnin.yadi.api;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceReference<T> {

    @Nonnull
    String getId();

    @Nonnull
    Class<T> getType();

    @Nonnull
    Optional<Supplier<T>> getSupplier();

    static <T> ServiceReference<T> serviceReference(
            @Nonnull final Class<T> type,
            @Nullable final String id,
            @Nullable final Supplier<T> supplier
    ) {
        return new ServiceReference<T>() {
            @Nonnull
            @Override
            public String getId() {
                return ofNullable(id).orElse(type.getCanonicalName());
            }

            @Nonnull
            @Override
            public Class<T> getType() {
                return type;
            }

            @Nonnull
            @Override
            public Optional<Supplier<T>> getSupplier() {
                return ofNullable(supplier);
            }

            @Override
            public String toString() {
                return "ServiceReference{" + "id=\"" + getId() + "\", type=\"" + getType() + "\"}";
            }

            @Override
            public int hashCode() {
                return Objects.hash(getId(), getType(), getSupplier());
            }

            @Override
            public boolean equals(final Object obj) {
                if (obj instanceof ServiceReference) {
                    final ServiceReference reference = (ServiceReference) obj;
                    return Objects.equals(getType(), reference.getType()) &&
                            Objects.equals(getId(), reference.getId()) &&
                            Objects.equals(getSupplier(), reference.getSupplier());
                } else {
                    return false;
                }
            }
        };
    }

    static <T> ServiceReference<T> serviceReference(@Nonnull final Class<T> type, @Nullable final String id) {
        return serviceReference(type, id, null);
    }

    static <T> ServiceReference<T> serviceReference(@Nonnull final Class<T> type, @Nullable final Supplier<T> supplier) {
        return serviceReference(type, null, supplier);
    }

    static <T> ServiceReference<T> serviceReference(@Nonnull final Class<T> type) {
        return serviceReference(type, null, null);
    }

    @Nonnull
    static ServiceReference<?>[] fromTypes(@Nonnull final Class<?>... types) {
        return stream(requireNonNull(types)).map(ServiceReference::serviceReference).toArray(ServiceReference<?>[]::new);
    }

    @Nonnull
    static Class<?>[] toTypes(@Nonnull final ServiceReference<?>... references) {
        return stream(requireNonNull(references)).map(ServiceReference::getType).toArray(Class<?>[]::new);
    }
}
