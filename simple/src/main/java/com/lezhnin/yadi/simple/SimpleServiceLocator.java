package com.lezhnin.yadi.simple;

import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceNotFoundException;
import com.lezhnin.yadi.api.ServiceProvider;
import com.lezhnin.yadi.api.ServiceProviderFinder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class SimpleServiceLocator implements ServiceLocator {

    private final ServiceProviderFinder serviceProviderFinder;
    private final List<ServiceLocator> parents;

    public SimpleServiceLocator(@Nonnull final ServiceProviderFinder serviceProviderFinder,
                                @Nonnull final List<ServiceLocator> parents) {
        this.serviceProviderFinder = requireNonNull(serviceProviderFinder);
        this.parents = requireNonNull(parents);
        for (ServiceLocator parent : parents) {
            requireNonNull(parent);
        }
    }

    @Nullable
    @Override
    public <T> Supplier<T> locate(@Nonnull final Class<T> beanType) {
        final ServiceProvider<T> provider = serviceProviderFinder.find(requireNonNull(beanType));
        if (provider != null) {
            return provider.apply(this);
        }
        for (ServiceLocator parent : parents) {
            final Supplier<T> serviceSupplier = parent.locate(beanType);
            if (serviceSupplier != null) {
                return serviceSupplier;
            }
        }
        throw new ServiceNotFoundException(beanType, this);
    }
}
