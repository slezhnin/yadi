package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceFinder {

    @Nullable
    <T> ServiceSupplier<T> find(@Nonnull Class<T> serviceType, @Nullable String serviceId);
}
