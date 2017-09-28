package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceBeanLocator;
import com.lezhnin.yadi.api.ServiceBeanNotFoundException;
import com.lezhnin.yadi.api.ServiceBeanProvider;
import com.lezhnin.yadi.api.ServiceBeanProviderFinder;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceBeanLocator implements ServiceBeanLocator {

    private final ServiceBeanProviderFinder serviceBeanProviderFinder;
    private final List<ServiceBeanLocator> parents;

    public SimpleServiceBeanLocator(@Nonnull final ServiceBeanProviderFinder serviceBeanProviderFinder,
                                    @Nonnull final List<ServiceBeanLocator> parents) {
        this.serviceBeanProviderFinder = requireNonNull(serviceBeanProviderFinder);
        this.parents = requireNonNull(parents);
        for (ServiceBeanLocator parent : parents) {
            requireNonNull(parent);
        }
    }

    @Nullable
    @Override
    public <T> T locate(@Nonnull final Class<T> beanType) {
        final ServiceBeanProvider<T> provider = serviceBeanProviderFinder.find(requireNonNull(beanType));
        if (provider != null) {
            return provider.provide(this);
        }
        for (ServiceBeanLocator parent : parents) {
            final T serviceBean = parent.locate(beanType);
            if (serviceBean != null) {
                return serviceBean;
            }
        }
        throw new ServiceBeanNotFoundException(beanType, this);
    }
}
