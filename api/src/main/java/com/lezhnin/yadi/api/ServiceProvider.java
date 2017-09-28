package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceProvider<T> {

    @Nonnull
    T provide(@Nonnull ServiceLocator serviceLocator);
}
