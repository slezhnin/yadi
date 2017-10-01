package com.lezhnin.yadi.api;

import java.util.Optional;
import java.util.function.Supplier;

public interface ServiceReference<T> {
    String getId();

    Class<T> getType();

    Optional<Supplier<T>> getImmediateSupplier();
}
