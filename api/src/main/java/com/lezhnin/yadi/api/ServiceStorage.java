package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceStorage {

    @Nullable
    <T> ServiceProvider<T> get(@Nonnull Class<T> serviceBeanInterface);

    @Nonnull
    <T> ServiceStorage put(@Nonnull Class<? extends T> serviceBeanInterface,
                           @Nonnull ServiceProvider<? extends T> serviceProvider);
}
