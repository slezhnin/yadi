package com.lezhnin.yadi.api.dependency;

import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.exception.MethodNotFoundException;
import com.lezhnin.yadi.api.ServiceReference;
import java.lang.reflect.Constructor;
import javax.annotation.Nonnull;

public interface ConstructorDependency<T> extends Dependency<T> {

    Constructor<T> getConstructor();

    static <T> ConstructorDependency<T> constructor(
            @Nonnull final ServiceReference<T> from,
            @Nonnull final Constructor<T> constructor,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return new ConstructorDependency<T>() {
            @Nonnull
            @Override
            public Constructor<T> getConstructor() {
                return requireNonNull(constructor);
            }

            @Nonnull
            @Override
            public ServiceReference<T> getTargetReference() {
                return requireNonNull(from);
            }

            @Nonnull
            @Override
            public ServiceReference<?>[] getReferences() {
                return requireNonNull(parameters);
            }
        };
    }

    static <T> Constructor<T> findConstructor(
            @Nonnull final Class<T> from,
            @Nonnull final Class<?>... parameters
    ) {
        try {
            return requireNonNull(from).getConstructor(requireNonNull(parameters));
        } catch (final NoSuchMethodException e) {
            throw new MethodNotFoundException(from, "constructor", parameters, e);
        }
    }

    static <T> ConstructorDependency<T> constructor(
            @Nonnull final ServiceReference<T> from,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return constructor(
                from,
                ConstructorDependency.findConstructor(
                        requireNonNull(from).getType(),
                        toTypes(parameters)
                ),
                parameters
        );
    }
}
