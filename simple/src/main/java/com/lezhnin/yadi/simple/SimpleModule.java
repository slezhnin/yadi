package com.lezhnin.yadi.simple;

import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import com.lezhnin.yadi.api.Dependency;
import com.lezhnin.yadi.api.Dependency.MethodDependency;
import com.lezhnin.yadi.api.MethodNotFoundException;
import com.lezhnin.yadi.api.ServiceConstructionException;
import com.lezhnin.yadi.api.ServiceDefinition;
import com.lezhnin.yadi.api.ServiceFinder;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceNotFoundException;
import com.lezhnin.yadi.api.ServiceReference;
import com.lezhnin.yadi.api.ServiceRegistry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleModule implements ServiceRegistry, ServiceFinder, ServiceLocator {

    private final ServiceFinder[] parents;
    private final Map<String, ServiceDefinition<?>> serviceDefinitionMap = new LinkedHashMap<>();

    public SimpleModule(@Nonnull final ServiceFinder... parents) {
        this.parents = requireNonNull(parents);
        this.selfRegistration();
    }

    public SimpleModule(@Nonnull final Consumer<ServiceRegistry> selfRegistration,
                        @Nonnull final ServiceFinder... parents) {
        this(parents);
        requireNonNull(selfRegistration).accept(this);
        this.selfRegistration();
    }

    @Override
    public void accept(@Nonnull final ServiceDefinition<?> serviceDefinition) {
        serviceDefinitionMap.put(requireNonNull(serviceDefinition).getReference().getId(), serviceDefinition);
    }

    protected void selfRegistration() {
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

    private <T> void postConstruction(final T instance, final Dependency[] dependencies) {
        stream(dependencies).forEach(d -> {
            if (d instanceof Dependency.InstanceMethodDependency) {
                try {
                    ((MethodDependency) d).getMethod().invoke(
                            ((Dependency.InstanceMethodDependency) d).getInstance(),
                            stream(d.getReferences()).map((Function<ServiceReference, Supplier>) this::locate).map(Supplier::get).toArray()
                    );
                } catch (final Exception exception) {
                    throw new MethodNotFoundException(
                            ((Dependency.InstanceMethodDependency) d).getMethod().getDeclaringClass(),
                            ((MethodDependency) d).getMethod().getName(),
                            ((MethodDependency) d).getMethod().getParameterTypes(),
                            exception
                    );
                }
            } else if (d instanceof MethodDependency) {
                try {
                    ((MethodDependency) d).getMethod().invoke(
                            instance,
                            stream(d.getReferences()).map((Function<ServiceReference, Supplier>) this::locate).map(Supplier::get).toArray()
                    );
                } catch (final Exception exception) {
                    throw new MethodNotFoundException(
                            instance.getClass(),
                            ((MethodDependency) d).getMethod().getName(),
                            ((MethodDependency) d).getMethod().getParameterTypes(),
                            exception
                    );
                }
            }
        });
    }

    private <T> T makeInstanceFrom(final ServiceDefinition<T> serviceDefinition) {
        try {
            return serviceDefinition.getReference().getType().getConstructor(
                    toTypes(serviceDefinition.getConstructionDependency().getReferences())
            ).newInstance(
                    stream(serviceDefinition.getConstructionDependency().getReferences())
                            .map((Function<ServiceReference, Supplier>) this::locate)
                            .map(Supplier::get)
                            .toArray()
            );
        } catch (final Exception e) {
            throw new ServiceConstructionException(serviceDefinition.getReference().getType(), e);
        }
    }
}
