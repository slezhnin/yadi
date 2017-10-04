package com.lezhnin.yadi.api;

import java.util.function.Supplier;
import javax.annotation.Nonnull;

public interface ServiceLocator {

    @Nonnull
    <T> Supplier<T> locate(@Nonnull ServiceReference<T> serviceReference);
}
