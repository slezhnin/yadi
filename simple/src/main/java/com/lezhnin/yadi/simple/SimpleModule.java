package com.lezhnin.yadi.simple;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceBinder;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceStorage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleModule extends SimpleServiceLocator implements ServiceBinder {

    private final ServiceBinder serviceBinder;

    protected SimpleModule(@Nonnull ServiceStorage storage, @Nonnull ServiceLocator... parents) {
        super(
                new SimpleServiceFinder(requireNonNull(storage)),
                asList(requireNonNull(parents))
        );
        serviceBinder = ServiceBinder.binder(storage);
        doBind();
    }

    protected SimpleModule(@Nonnull ServiceLocator... parents) {
        this(new SimpleServiceStorage(), parents);
    }

    @Nonnull
    public static SimpleModule simpleModule(@Nonnull ServiceLocator... parents) {
        return new SimpleModule(parents);
    }

    @Nonnull
    @Override
    public <T> ToSupplier<T> bind(@Nonnull final Class<T> serviceType, @Nullable final String serviceId) {
        return serviceBinder.bind(serviceType, serviceId);
    }

    protected void doBind() {
    }
}
