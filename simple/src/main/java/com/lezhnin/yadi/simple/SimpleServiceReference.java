package com.lezhnin.yadi.simple;

import com.lezhnin.yadi.api.ServiceReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public final class SimpleServiceReference<T> implements ServiceReference<T> {

    private final String id;
    private final Class<T> type;
    private final Supplier<T> immediateSupplier;

    private SimpleServiceReference(@Nonnull String id, @Nonnull Class<T> type, @Nullable Supplier<T> immediateSupplier) {
        this.id = requireNonNull(id);
        this.type = type;
        this.immediateSupplier = immediateSupplier;
    }

    @Nonnull
    public static <T> ServiceReference<T> reference(@Nonnull Class<T> type) {
        return new SimpleServiceReference<>(requireNonNull(type).getName(), type, null);
    }

    @Nonnull
    public static <T> ServiceReference<T> reference(@Nonnull String id, @Nonnull Class<T> type) {
        return new SimpleServiceReference<>(id, type, null);
    }

    @Nonnull
    public static <T> ServiceReference<T> immediate(@Nonnull Class<T> type, @Nonnull Supplier<T> provider) {
        return new SimpleServiceReference<>(
                valueOf(provider.getClass().getName()),
                requireNonNull(type),
                provider
        );
    }

    @Nonnull
    public static <T> ServiceReference<T> immediate(@Nonnull Class<T> type, @Nonnull T value) {
        return new SimpleServiceReference<>(
                valueOf(requireNonNull(value).hashCode()),
                requireNonNull(type),
                new ImmediateSupplier<>(value)
        );
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public Optional<Supplier<T>> getImmediateSupplier() {
        return ofNullable(immediateSupplier);
    }
}
