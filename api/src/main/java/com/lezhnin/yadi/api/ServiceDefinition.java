package com.lezhnin.yadi.api;

import static java.util.Objects.requireNonNull;
import java.util.Objects;
import javax.annotation.Nonnull;

public interface ServiceDefinition<T> {

    @Nonnull
    ServiceReference<T> getReference();

    @Nonnull
    Dependency<T> getConstructionDependency();

    @Nonnull
    Dependency[] getPostConstructionDependencies();

    static <T> ServiceDefinition<T> serviceDefinition(final ServiceReference<? extends T> serviceReference,
                                                      @Nonnull final Dependency<? extends T> constructionDependency,
                                                      @Nonnull final Dependency... postConstructionDependency) {
        return new ServiceDefinition<T>() {
            @SuppressWarnings("unchecked")
            @Nonnull
            @Override
            public ServiceReference<T> getReference() {
                return (ServiceReference<T>) serviceReference;
            }

            @SuppressWarnings("unchecked")
            @Nonnull
            @Override
            public Dependency<T> getConstructionDependency() {
                return (Dependency<T>) constructionDependency;
            }

            @Nonnull
            @Override
            public Dependency[] getPostConstructionDependencies() {
                return requireNonNull(postConstructionDependency);
            }
        };
    }
}
