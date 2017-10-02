package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;

public interface ServiceBinder {

    @Nonnull
    <T> ServiceImplementor<T> bind(@Nonnull String serviceId);

    @Nonnull
    <T> ServiceImplementor<T> bind(@Nonnull Class<T> serviceInterface);
}
