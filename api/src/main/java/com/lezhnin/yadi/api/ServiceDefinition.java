package com.lezhnin.yadi.api;

import static java.util.Objects.requireNonNull;
import javax.annotation.Nonnull;

public interface ServiceDefinition<T> {

    @Nonnull
    ServiceReference<T> getReference();

    @Nonnull
    Dependency<T> getConstructionDependency();

    @Nonnull
    Dependency[] getPostConstructionDependencies();

    static <T> ServiceDefinition<T> serviceDefinition(@Nonnull final ServiceReference<T> serviceReference,
                                                      @Nonnull final Dependency<T> constructionDependency,
                                                      @Nonnull final Dependency... postConstructionDependency) {
        return new ServiceDefinition<T>() {
            @Nonnull
            @Override
            public ServiceReference<T> getReference() {
                return requireNonNull(serviceReference);
            }

            @Nonnull
            @Override
            public Dependency<T> getConstructionDependency() {
                return requireNonNull(constructionDependency);
            }

            @Nonnull
            @Override
            public Dependency[] getPostConstructionDependencies() {
                return requireNonNull(postConstructionDependency);
            }
        };
    }
}
