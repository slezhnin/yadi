package com.lezhnin.yadi.api;

import java.util.function.Function;
import java.util.function.Supplier;

public interface ServiceProvider<T> extends Function<ServiceLocator, Supplier<T>> {
}
