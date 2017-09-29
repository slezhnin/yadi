package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;

public interface ServiceProvider<T> {

    @Nonnull
    T provide(@Nonnull ServiceLocator serviceLocator);
}
