package com.lezhnin.yadi.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public interface ServiceLocator {

    @Nullable
    <T> Supplier<T> locate(@Nonnull Class<T> beanType);
}
