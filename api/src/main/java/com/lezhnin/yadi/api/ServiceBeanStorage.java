package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceBeanStorage {

    @Nullable
    <T> ServiceBeanProvider<T> get(@Nonnull Class<T> serviceBeanInterface);

    @Nonnull
    <T> ServiceBeanStorage put(@Nonnull Class<T> serviceBeanInterface, @Nonnull ServiceBeanProvider<T> serviceBeanProvider);
}
