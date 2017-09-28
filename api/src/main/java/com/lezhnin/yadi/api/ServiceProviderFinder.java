package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceProviderFinder {

    @Nullable
    <T> ServiceProvider<T> find(@Nonnull Class<T> serviceBean);
}
