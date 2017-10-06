package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceDefinition;
import com.lezhnin.yadi.api.ServiceFinder;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceNotFoundException;
import com.lezhnin.yadi.api.ServiceReference;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public class SimpleLocator implements ServiceLocator {

    private final ServiceFinder serviceFinder;

    public SimpleLocator(final ServiceFinder serviceFinder) {
        this.serviceFinder = serviceFinder;
    }

    @Nonnull
    @Override
    public <T> Supplier<T> locate(@Nonnull final ServiceReference<T> serviceReference) {
        final ServiceDefinition<T> serviceDefinition = serviceFinder.find(requireNonNull(serviceReference));
        if (serviceDefinition == null) {
            throw new ServiceNotFoundException(serviceReference, this);
        }
        return () -> postConstruct(
                new SimpleInstance<T>(this).apply(serviceDefinition)
        ).apply(
                serviceDefinition.getPostConstructionDependencies()
        );
    }

    private <T> SimplePostConstruct<T> postConstruct(final T instance) {
        return new SimplePostConstruct<>(instance, this);
    }
}
