package com.lezhnin.yadi.api;

import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Objects.requireNonNull;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

public interface InstanceMethodDependency<F, T> extends MethodDependency<F, T> {

    @Nonnull
    F getInstance();

    static <F, T> InstanceMethodDependency<F, T> methodFromInstance(
            @Nonnull final F instance,
            @Nonnull final ServiceReference<T> targetReference,
            @Nonnull final Method method,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return new InstanceMethodDependency<F, T>() {
            @Nonnull
            @Override
            public F getInstance() {
                return requireNonNull(instance);
            }

            @Nonnull
            @Override
            public ServiceReference<T> getTargetReference() {
                return requireNonNull(targetReference);
            }

            @SuppressWarnings("unchecked")
            @Nonnull
            @Override
            public ServiceReference<F> getSourceReference() {
                return serviceReference((Class<F>) requireNonNull(instance).getClass());
            }

            @Nonnull
            @Override
            public Method getMethod() {
                return requireNonNull(method);
            }

            @Nonnull
            @Override
            public ServiceReference<?>[] getReferences() {
                return requireNonNull(parameters);
            }
        };
    }

    static <F, T> InstanceMethodDependency<F, T> methodFromInstance(
            @Nonnull final F instance,
            @Nonnull final ServiceReference<T> targetReference,
            @Nonnull final String name,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return methodFromInstance(
                instance,
                targetReference,
                MethodDependency.findMethodInClass(
                        requireNonNull(instance).getClass(),
                        requireNonNull(name),
                        toTypes(parameters)),
                parameters
        );
    }

    @SuppressWarnings("unchecked")
    static <T> InstanceMethodDependency<T, T> methodFromInstance(
            @Nonnull final T instance,
            @Nonnull final String name,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return methodFromInstance(
                instance,
                serviceReference((Class<T>) requireNonNull(instance).getClass()),
                MethodDependency.findMethodInClass(
                        instance.getClass(),
                        requireNonNull(name),
                        toTypes(parameters)),
                parameters
        );
    }
}
