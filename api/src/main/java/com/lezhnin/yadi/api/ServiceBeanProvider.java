package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceBeanProvider<T> {

    @Nullable
    T provide(@Nonnull ServiceBeanLocator serviceBeanLocator);
}
