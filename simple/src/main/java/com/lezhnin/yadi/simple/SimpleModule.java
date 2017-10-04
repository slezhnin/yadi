package com.lezhnin.yadi.simple;

import com.lezhnin.yadi.api.*;
import com.lezhnin.yadi.api.Dependency.MethodDependency;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class SimpleModule implements ServiceRegistry, ServiceFinder, ServiceLocator {

    private final ServiceFinder[] parents;
    private final Map<String, ServiceDefinition<?>> serviceDefinitionMap = new LinkedHashMap<>();

    public SimpleModule(@Nonnull final ServiceFinder... parents) {
        this.parents = requireNonNull(parents);
        this.selfRegistration();
    }

    public SimpleModule(@Nonnull final Function<ServiceRegistry, Void> selfRegistration,
                        @Nonnull final ServiceFinder... parents) {
        this(parents);
        requireNonNull(selfRegistration).apply(this);
        this.selfRegistration();
    }

    protected void selfRegistration() {};

    @Nonnull
    @Override
    public ServiceRegistry register(@Nonnull final ServiceDefinition<?> serviceDefinition) {
        serviceDefinitionMap.put(requireNonNull(serviceDefinition).getReference().getId(), serviceDefinition);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> ServiceDefinition<T> find(@Nonnull final ServiceReference<T> serviceReference) {
        final String serviceId = requireNonNull(serviceReference).getId();
        final ServiceDefinition<T> definition = (ServiceDefinition<T>) serviceDefinitionMap.get(serviceId);
        return ofNullable(definition).orElse(
                stream(parents).map(p -> p.find(serviceReference))
                        .filter(Objects::nonNull)
                        .findFirst().orElse(null)
        );
    }

    @Nonnull
    @Override
    public <T> Supplier<T> locate(@Nonnull final ServiceReference<T> serviceReference) {
        final ServiceDefinition<T> serviceDefinition = find(serviceReference);
        if (serviceDefinition == null) {
            throw new ServiceNotFoundException(serviceReference, this);
        }
        return () -> {
            final T instance = makeInstanceFrom(serviceDefinition);
            postConstruction(instance, serviceDefinition.getPostConstructionDependencies());
            return instance;
        };
    }

    private <T> void postConstruction(T instance, Dependency[] dependencies) {
        stream(dependencies).forEach(d -> {
            if (d instanceof Dependency.InstanceMethodDependency) {
                try {
                    ((MethodDependency) d).getMethod().invoke(
                            ((Dependency.InstanceMethodDependency) d).getInstance(),
                            stream(d.getReferences()).map(this::locate).map(Supplier::get)
                    );
                } catch (Exception e) {
                    throw new MethodNotFoundException(
                            ((Dependency.InstanceMethodDependency) d).getMethod().getDeclaringClass(),
                            ((MethodDependency) d).getMethod().getName(),
                            ((MethodDependency) d).getMethod().getParameterTypes(),
                            e
                    );
                }
            } else if (d instanceof MethodDependency) {
                try {
                    ((MethodDependency) d).getMethod().invoke(
                            instance,
                            stream(d.getReferences()).map(this::locate).map(Supplier::get)
                    );
                } catch (Exception e) {
                    throw new MethodNotFoundException(
                            instance.getClass(),
                            ((MethodDependency) d).getMethod().getName(),
                            ((MethodDependency) d).getMethod().getParameterTypes(),
                            e
                    );
                }
            }
        });
    }

    private <T> T makeInstanceFrom(ServiceDefinition<T> serviceDefinition) {
        try {
            return serviceDefinition.getReference().getType().getConstructor(
                    toTypes(serviceDefinition.getConstructionDependency().getReferences())
            ).newInstance(
                    stream(serviceDefinition.getConstructionDependency().getReferences())
                            .map(this::locate)
                            .map(Supplier::get)
                            .toArray()
            );
        } catch (Exception e) {
            throw new ServiceConstructionException(serviceDefinition.getReference().getType(), e);
        }
    }
}
