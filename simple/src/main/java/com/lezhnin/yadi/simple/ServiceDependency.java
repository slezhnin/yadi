package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import javax.annotation.Nonnull;

public abstract class ServiceDependency {

    private final Class<?>[] dependencies;

    public ServiceDependency(@Nonnull final Class<?>[] dependencies) {
        this.dependencies = requireNonNull(dependencies);
    }

    public Class<?>[] getDependencies() {
        return dependencies;
    }
}
