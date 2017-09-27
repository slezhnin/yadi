package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceBeanProviderFinder;
import com.lezhnin.yadi.api.ServiceBeanProvider;
import com.lezhnin.yadi.api.ServiceBeanStorage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleServiceBeanProviderFinder implements ServiceBeanProviderFinder {

    private final ServiceBeanStorage serviceBeanStorage;

    public SimpleServiceBeanProviderFinder(@Nonnull ServiceBeanStorage serviceBeanStorage) {
        this.serviceBeanStorage = requireNonNull(serviceBeanStorage);
    }

    @Nullable
    @Override
    public <T> ServiceBeanProvider<T> find(@Nonnull final Class<T> serviceBean) {
        return serviceBeanStorage.get(serviceBean);
    }
}
