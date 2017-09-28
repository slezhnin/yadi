package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;

public interface ServiceBeanImplementor<T> {

    @Nonnull
    ServiceBeanBinder to(@Nonnull ServiceBeanProvider<? extends T> serviceBeanProvider);
}
