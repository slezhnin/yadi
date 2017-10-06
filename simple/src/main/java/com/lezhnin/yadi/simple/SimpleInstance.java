package com.lezhnin.yadi.simple;

import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Arrays.stream;
import com.lezhnin.yadi.api.ServiceConstructionException;
import com.lezhnin.yadi.api.ServiceDefinition;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceReference;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleInstance<T> implements Function<ServiceDefinition<T>, T> {

    private final ServiceLocator serviceLocator;

    public SimpleInstance(final ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public T apply(final ServiceDefinition<T> serviceDefinition) {
        try {
            return serviceDefinition.getReference().getType().getConstructor(
                    toTypes(serviceDefinition.getConstructionDependency().getReferences())
            ).newInstance(
                    stream(serviceDefinition.getConstructionDependency().getReferences())
                            .map((Function<ServiceReference, Supplier>) serviceLocator::locate)
                            .map(Supplier::get)
                            .toArray()
            );
        } catch (final Exception e) {
            throw new ServiceConstructionException(serviceDefinition.getReference().getType(), e);
        }
    }
}
