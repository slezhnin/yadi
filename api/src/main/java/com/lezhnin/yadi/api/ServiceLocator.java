package com.lezhnin.yadi.api;

import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public interface ServiceLocator {

    @Nonnull
    <T> Supplier<T> locate(@Nonnull ServiceReference<T> serviceReference);

    @Nonnull
    default <T> Supplier<T> locate(@Nonnull final Class<T> serviceClass) {
        return locate(serviceReference(serviceClass));
    }
}
