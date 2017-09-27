package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ServiceBeanLocator {

    @Nullable
    <T> T locate(@Nonnull Class<T> beanType);
}
