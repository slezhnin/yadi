package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;

public interface ServiceBeanBinder {

    @Nonnull
    <T> ServiceBeanImplementor<T> bind(@Nonnull Class<T> serviceBeanInterface);
}
