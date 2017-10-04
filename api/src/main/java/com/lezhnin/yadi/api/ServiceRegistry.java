package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceRegistry extends ServiceLocator {

    @Nonnull
    ServiceRegistry register(@Nonnull ServiceDefinition<?> serviceDefinition);

    @Nullable
    <T> ServiceDefinition<T> find(@Nonnull ServiceReference<T> serviceReference);
}
