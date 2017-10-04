package com.lezhnin.yadi.simple;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import com.lezhnin.yadi.api.ServiceDefinition;
import com.lezhnin.yadi.api.ServiceNotFoundException;
import com.lezhnin.yadi.api.ServiceReference;
import com.lezhnin.yadi.api.ServiceRegistry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleRegistry implements ServiceRegistry {

    private final ServiceRegistry[] parents;
    private final Map<String, ServiceDefinition<?>> serviceDefinitionMap = new LinkedHashMap<>();

    public SimpleRegistry(@Nonnull final ServiceRegistry... parents) {
        this.parents = parents;
    }

    @Nonnull
    @Override
    public ServiceRegistry register(@Nonnull final ServiceDefinition<?> serviceDefinition) {
        serviceDefinitionMap.put(serviceDefinition.getReference().getId(), serviceDefinition);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> ServiceDefinition<T> find(@Nonnull final ServiceReference<T> serviceReference) {
        final ServiceDefinition<T> definition = (ServiceDefinition<T>) serviceDefinitionMap.get(serviceReference.getId());
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
    }
}
