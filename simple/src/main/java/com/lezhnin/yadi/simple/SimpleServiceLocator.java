package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceNotFoundException;
import com.lezhnin.yadi.api.ServiceProvider;
import com.lezhnin.yadi.api.ServiceProviderFinder;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    public <T> T locate(@Nonnull final Class<T> beanType) {
        final ServiceProvider<T> provider = serviceProviderFinder.find(requireNonNull(beanType));
        if (provider != null) {
            return provider.provide(this);
        }
        for (ServiceLocator parent : parents) {
            final T serviceBean = parent.locate(beanType);
            if (serviceBean != null) {
                return serviceBean;
            }
        }
        throw new ServiceNotFoundException(beanType, this);
    }
}
