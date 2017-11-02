package com.lezhnin.yadi.api;

import com.lezhnin.yadi.api.dependency.Dependency;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NonNull;

@Data
public class ServiceDefinition<T> {

    @NonNull
    private final ServiceReference<T> reference;

    @NonNull
    private final Dependency construct;

    @NonNull
    private final Dependency[] postConstruct;

    @SuppressWarnings("unchecked")
    public static <T> ServiceDefinition<T> serviceDefinition(@Nonnull final ServiceReference<? extends T> serviceReference,
                                                             @Nonnull final Dependency constructionDependency,
                                                             @Nonnull final Dependency... postConstructionDependency) {
        return new ServiceDefinition<>(
                (ServiceReference<T>) serviceReference,
                constructionDependency,
                postConstructionDependency
        );
    }
}
