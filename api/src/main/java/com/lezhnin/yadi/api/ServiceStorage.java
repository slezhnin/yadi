package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceStorage {

    @Nullable
    <T> ServiceProvider<T> get(@Nonnull String serviceId);

    @Nonnull
    <T> ServiceStorage put(@Nonnull String serviceId,
                           @Nonnull ServiceProvider<? extends T> serviceProvider);
}
