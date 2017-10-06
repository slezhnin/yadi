package com.lezhnin.yadi.simple;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import com.lezhnin.yadi.api.ServiceDefinition;
import com.lezhnin.yadi.api.ServiceFinder;
import com.lezhnin.yadi.api.ServiceReference;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleFinder implements ServiceFinder {

    private final Map<String, ServiceDefinition<?>> serviceDefinitionMap;
    private final ServiceFinder[] parents;

    public SimpleFinder(@Nonnull final Map<String, ServiceDefinition<?>> serviceDefinitionMap, @Nonnull final ServiceFinder... parents) {
        this.serviceDefinitionMap = requireNonNull(serviceDefinitionMap);
        this.parents = requireNonNull(parents);
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
}
