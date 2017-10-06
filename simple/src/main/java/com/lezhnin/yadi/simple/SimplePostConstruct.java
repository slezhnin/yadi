package com.lezhnin.yadi.simple;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.Dependency;
import com.lezhnin.yadi.api.Dependency.InstanceMethodDependency;
import com.lezhnin.yadi.api.Dependency.MethodDependency;
import com.lezhnin.yadi.api.MethodNotFoundException;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public class SimplePostConstruct<T> implements Function<Dependency[], T> {

    private final T instance;
    private final ServiceLocator serviceLocator;

    public SimplePostConstruct(@Nonnull final T instance, @Nonnull final ServiceLocator serviceLocator) {
        this.instance = requireNonNull(instance);
        this.serviceLocator = requireNonNull(serviceLocator);
    }

    @Override
    public T apply(final Dependency[] dependencies) {
        stream(dependencies).forEach(d -> {
            if (d instanceof InstanceMethodDependency) {
                invokeMethod(((InstanceMethodDependency) d).getInstance(), (MethodDependency) d);
            } else if (d instanceof MethodDependency) {
                invokeMethod(instance, (MethodDependency) d);
            }
        });
        return instance;
    }

    private void invokeMethod(final Object instance, final MethodDependency methodDependency) {
        try {
            methodDependency.getMethod().invoke(
                    instance, referencesToObjects(methodDependency.getReferences())
            );
        } catch (final Exception exception) {
            throw new MethodNotFoundException(
                    instance.getClass(),
                    methodDependency.getMethod().getName(),
                    methodDependency.getMethod().getParameterTypes(),
                    exception
            );
        }
    }

    private Object[] referencesToObjects(final ServiceReference<?>[] references) {
        return stream(references)
                .map(this::locate)
                .map(Supplier::get)
                .toArray();
    }

    private Supplier<?> locate(final ServiceReference<?> reference) {
        return serviceLocator.locate(reference);
    }
}
