package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;

public interface ServiceRegistry {

    @Nonnull
    ServiceRegistry register(@Nonnull ServiceDefinition<?> serviceDefinition);
}
