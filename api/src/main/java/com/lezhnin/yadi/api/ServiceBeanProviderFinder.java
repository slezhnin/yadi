package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceBeanProviderFinder {

    @Nullable
    <T> ServiceBeanProvider<T> find(@Nonnull Class<T> serviceBean);
}
