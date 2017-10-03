package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceLocator {

    @Nonnull
    <T> ServiceSupplier<T> locate(@Nonnull ServiceReference<T> serviceReference);
}
