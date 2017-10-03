package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceStorage {

    @Nullable
    <T> ServiceSupplier<T> get(@Nonnull String serviceId);

    @Nonnull
    <T> ServiceStorage put(@Nonnull String serviceId,
                           @Nonnull ServiceSupplier<? extends T> serviceSupplier);
}
