package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceBeanBinder;
import com.lezhnin.yadi.api.ServiceBeanImplementor;
import com.lezhnin.yadi.api.ServiceBeanProvider;
import com.lezhnin.yadi.api.ServiceBeanStorage;
import javax.annotation.Nonnull;

public class SimpleServiceBeanBinder implements ServiceBeanBinder {

    private final ServiceBeanStorage serviceBeanStorage;

    public SimpleServiceBeanBinder(@Nonnull final ServiceBeanStorage serviceBeanStorage) {
        this.serviceBeanStorage = requireNonNull(serviceBeanStorage);
    }

    @Nonnull
    @Override
    public <T> ServiceBeanImplementor<T> bind(@Nonnull final Class<? extends T> serviceBeanInterface) {
        return new ServiceBeanImplementor<T>() {
            @Nonnull
            @Override
            public ServiceBeanBinder to(@Nonnull final ServiceBeanProvider<? extends T> serviceBeanProvider) {
                serviceBeanStorage.put(requireNonNull(serviceBeanInterface), requireNonNull(serviceBeanProvider));
                return SimpleServiceBeanBinder.this;
            }
        };
    }
}
