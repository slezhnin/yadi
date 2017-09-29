package com.lezhnin.yadi.simple;

import javax.annotation.Nonnull;

public class ConstructorDependency extends ServiceDependency {

    private ConstructorDependency(@Nonnull final Class<?>[] dependencies) {
        super(dependencies);
    }

    public static ServiceDependency constructor(@Nonnull final Class<?>... dependencies) {
        return new ConstructorDependency(dependencies);
    }
}
