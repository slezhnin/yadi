package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceDefinition;
import com.lezhnin.yadi.api.ServiceFinder;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceReference;
import com.lezhnin.yadi.api.ServiceRegistry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleModule implements ServiceRegistry, ServiceFinder, ServiceLocator {

    private final ServiceRegistry serviceRegistry;
    private final ServiceFinder serviceFinder;
    private final ServiceLocator serviceLocator;

    protected SimpleModule(@Nonnull final ServiceRegistry serviceRegistry,
                           @Nonnull final ServiceFinder serviceFinder,
                           @Nonnull final ServiceLocator serviceLocator) {
        this.serviceRegistry = requireNonNull(serviceRegistry);
        this.serviceFinder = requireNonNull(serviceFinder);
        this.serviceLocator = requireNonNull(serviceLocator);
        this.selfRegistration();
    }

    protected SimpleModule(@Nonnull final ServiceFinder... parents) {
        final Map<String, ServiceDefinition<?>> serviceDefinitionMap = new LinkedHashMap<>();
        this.serviceRegistry = new SimpleRegistry(serviceDefinitionMap);
        this.serviceFinder = new SimpleFinder(serviceDefinitionMap, parents);
        this.serviceLocator = new SimpleLocator(serviceFinder);
        this.selfRegistration();
    }

    public static SimpleModule module(@Nonnull final ServiceFinder... parents) {
        return new SimpleModule(parents);
    }

    public static SimpleModule moduleWithRegistration(@Nonnull final Consumer<ServiceRegistry> selfRegistration,
                                                      @Nonnull final ServiceFinder... parents) {
        final SimpleModule module = module(parents);
        requireNonNull(selfRegistration).accept(module);
        return module;
    }

    protected void selfRegistration() {
    }

    @Override
    public void accept(@Nonnull final ServiceDefinition<?> serviceDefinition) {
        serviceRegistry.accept(serviceDefinition);
    }

    @Nullable
    @Override
    public <T> ServiceDefinition<T> find(@Nonnull final ServiceReference<T> serviceReference) {
        return serviceFinder.find(serviceReference);
    }

    @Nonnull
    @Override
    public <T> Supplier<T> locate(@Nonnull final ServiceReference<T> serviceReference) {
        return serviceLocator.locate(serviceReference);
    }
}
