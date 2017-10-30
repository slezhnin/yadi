package com.lezhnin.yadi.api;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Wither;

@Data
public class ServiceReference<T> {

    @NonNull
    private final Class<T> type;

    @NonNull
    private final String id;

    @Wither
    private final Supplier<T> supplier;

    public static <T> ServiceReference<T> serviceReference(@Nonnull final Class<T> type, final String id) {
        return new ServiceReference<>(type, ofNullable(id).orElse(requireNonNull(type).getCanonicalName()), null);
    }

    public static <T> ServiceReference<T> serviceReference(@Nonnull final Class<T> type, final Supplier<T> supplier) {
        return serviceReference(type, (String) null).withSupplier(supplier);
    }

    public static <T> ServiceReference<T> serviceReference(@Nonnull final Class<T> type) {
        return serviceReference(type, (String) null);
    }

    @Nonnull
    public static ServiceReference<?>[] fromTypes(@Nonnull final Class<?>... types) {
        return stream(requireNonNull(types)).map(ServiceReference::serviceReference).toArray(ServiceReference<?>[]::new);
    }

    @Nonnull
    public static Class<?>[] toTypes(@Nonnull final ServiceReference<?>... references) {
        return stream(requireNonNull(references)).map(ServiceReference::getType).toArray(Class<?>[]::new);
    }
}
