package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceFinder;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceNotFoundException;
import com.lezhnin.yadi.api.ServiceSupplier;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceLocator implements ServiceLocator {

    private final ServiceFinder serviceFinder;
    private final List<ServiceLocator> parents;

    public SimpleServiceLocator(@Nonnull final ServiceFinder serviceFinder,
                                @Nonnull final List<ServiceLocator> parents) {
        this.serviceFinder = requireNonNull(serviceFinder);
        this.parents = requireNonNull(parents);
        for (final ServiceLocator parent : parents) {
            requireNonNull(parent);
        }
    }

    @Nullable
    @Override
    public <T> Supplier<T> locate(@Nonnull final Class<T> beanType, @Nullable final String serviceId) {
        final ServiceSupplier<T> provider = serviceFinder.find(requireNonNull(beanType), serviceId);
        if (provider != null) {
            return provider.apply(this);
        }
        for (final ServiceLocator parent : parents) {
            final Supplier<T> serviceSupplier = parent.locate(beanType, serviceId);
            if (serviceSupplier != null) {
                return serviceSupplier;
            }
        }
        throw new ServiceNotFoundException(beanType, this);
    }
}
