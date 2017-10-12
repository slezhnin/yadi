package com.lezhnin.yadi.api.dependency;

import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.exception.MethodNotFoundException;
import com.lezhnin.yadi.api.ServiceReference;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

public interface MethodDependency<F, T> extends Dependency<T> {

    @Nonnull
    ServiceReference<F> getSourceReference();

    @Nonnull
    Method getMethod();

    static <F, T> MethodDependency<F, T> methodFromClass(
            @Nonnull final ServiceReference<F> sourceReference,
            @Nonnull final ServiceReference<T> targetReference,
            @Nonnull final Method method,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return new MethodDependency<F, T>() {
            @Nonnull
            @Override
            public ServiceReference<T> getTargetReference() {
                return requireNonNull(targetReference);
            }

            @Nonnull
            @Override
            public ServiceReference<F> getSourceReference() {
                return requireNonNull(sourceReference);
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

    static <F, T> MethodDependency<F, T> methodFromClass(
            @Nonnull final ServiceReference<F> sourceReference,
            @Nonnull final ServiceReference<T> targetReference,
            @Nonnull final String name,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return methodFromClass(
                requireNonNull(sourceReference),
                requireNonNull(targetReference),
                MethodDependency.findMethodInClass(
                        sourceReference.getType(),
                        requireNonNull(name),
                        toTypes(requireNonNull(parameters))
                ),
                parameters
        );
    }

    static <T> MethodDependency<T, T> methodFromClass(
            @Nonnull final ServiceReference<T> targetReference,
            @Nonnull final String name,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return methodFromClass(
                requireNonNull(targetReference),
                requireNonNull(targetReference),
                MethodDependency.findMethodInClass(
                        targetReference.getType(),
                        requireNonNull(name),
                        toTypes(requireNonNull(parameters))
                ),
                parameters
        );
    }

    static Method findMethodInClass(
            @Nonnull final Class<?> from,
            @Nonnull final String name,
            @Nonnull final Class<?>... parameters
    ) {
        try {
            return requireNonNull(from).getMethod(requireNonNull(name), requireNonNull(parameters));
        } catch (final NoSuchMethodException e) {
            throw new MethodNotFoundException(from, name, parameters, e);
        }
    }
}
