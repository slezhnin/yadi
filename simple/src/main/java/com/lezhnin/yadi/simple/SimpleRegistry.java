package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceDefinition;
import com.lezhnin.yadi.api.ServiceRegistry;
import java.util.Map;
import javax.annotation.Nonnull;

public class SimpleRegistry implements ServiceRegistry {

    private final Map<String, ServiceDefinition<?>> serviceDefinitionMap;

    public SimpleRegistry(@Nonnull final Map<String, ServiceDefinition<?>> serviceDefinitionMap) {
        this.serviceDefinitionMap = requireNonNull(serviceDefinitionMap);
    }

    @Override
    public void accept(@Nonnull final ServiceDefinition<?> serviceDefinition) {
        serviceDefinitionMap.put(requireNonNull(serviceDefinition).getReference().getId(), serviceDefinition);
    }
}
