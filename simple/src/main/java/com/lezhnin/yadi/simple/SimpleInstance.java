package com.lezhnin.yadi.simple;

import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Arrays.stream;
import com.lezhnin.yadi.api.Dependency.ConstructorDependency;
import com.lezhnin.yadi.api.Dependency.InstanceMethodDependency;
import com.lezhnin.yadi.api.Dependency.MethodDependency;
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

    @SuppressWarnings("unchecked")
    @Override
    public T apply(final ServiceDefinition<T> serviceDefinition) {
        try {
            final Object[] args = stream(serviceDefinition.getConstructionDependency().getReferences())
                    .map((Function<ServiceReference, Supplier>) serviceLocator::locate)
                    .map(Supplier::get)
                    .toArray();
            if (serviceDefinition.getConstructionDependency() instanceof ConstructorDependency) {
                return ((ConstructorDependency<T>) serviceDefinition.getConstructionDependency()).getConstructor().newInstance(args);
            } else if (serviceDefinition.getConstructionDependency() instanceof MethodDependency) {
                final MethodDependency<?, T> methodDependency = (MethodDependency<?, T>) serviceDefinition.getConstructionDependency();
                return (T) methodDependency.getMethod().invoke(serviceLocator.locate(methodDependency.getSourceReference()).get(), args);
            } else if (serviceDefinition.getConstructionDependency() instanceof InstanceMethodDependency) {
                final InstanceMethodDependency<?, T> methodDependency = (InstanceMethodDependency<?, T>) serviceDefinition.getConstructionDependency();
                return (T) methodDependency.getMethod().invoke(methodDependency.getInstance(), args);
            }
            // TODO: Should we allow this default behaviour?
            return serviceDefinition.getReference().getType().getConstructor(
                    toTypes(serviceDefinition.getConstructionDependency().getReferences())
            ).newInstance(args);
        } catch (final Exception e) {
            throw new ServiceConstructionException(serviceDefinition.getReference().getType(), e);
        }
    }
}
