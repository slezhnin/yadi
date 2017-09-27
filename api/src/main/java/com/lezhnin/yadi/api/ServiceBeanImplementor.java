package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;

public interface ServiceBeanImplementor<T> {

    @Nonnull
    ServiceBeanBinder to(@Nonnull ServiceBeanProvider<T> serviceBeanProvider);
}
