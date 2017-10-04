package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceFinder {

    @Nullable
    <T> ServiceDefinition<T> find(@Nonnull ServiceReference<T> serviceReference);
}
