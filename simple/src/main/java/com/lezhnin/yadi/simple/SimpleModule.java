package com.lezhnin.yadi.simple;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceBeanBinder;
import com.lezhnin.yadi.api.ServiceBeanImplementor;
import com.lezhnin.yadi.api.ServiceBeanLocator;
import com.lezhnin.yadi.api.ServiceBeanStorage;
import javax.annotation.Nonnull;

public class SimpleModule extends SimpleServiceBeanLocator implements ServiceBeanBinder {

    private final ServiceBeanBinder serviceBeanBinder;

    protected SimpleModule(@Nonnull ServiceBeanStorage storage, @Nonnull ServiceBeanLocator... parents) {
        super(
                new SimpleServiceBeanProviderFinder(requireNonNull(storage)),
                asList(requireNonNull(parents))
        );
        serviceBeanBinder = new SimpleServiceBeanBinder(storage);
        doBind();
    }

    protected SimpleModule(@Nonnull ServiceBeanLocator... parents) {
        this(new SimpleServiceBeanStorage(), parents);
    }

    @Nonnull
    public static SimpleModule simpleModule(@Nonnull ServiceBeanLocator... parents) {
        return new SimpleModule(parents);
    }

    @Nonnull
    @Override
    public <T> ServiceBeanImplementor<T> bind(@Nonnull final Class<? extends T> serviceBeanInterface) {
        return serviceBeanBinder.bind(requireNonNull(serviceBeanInterface));
    }

    protected void doBind() {
    }
}
