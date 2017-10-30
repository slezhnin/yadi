package com.lezhnin.yadi.api.dependency;

import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceReference;
import com.lezhnin.yadi.api.exception.MethodNotFoundException;
import java.lang.reflect.Constructor;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NonNull;

@Data
public class ConstructorDependency<T> implements Dependency {

    @NonNull
    private final Constructor<T> constructor;

    @NonNull
    private final ServiceReference<?>[] parameters;

    public static <T> ConstructorDependency<T> constructor(
            @Nonnull final Constructor<T> constructor,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return new ConstructorDependency<>(constructor, parameters);
    }

    private static <T> Constructor<T> findConstructor(
            @Nonnull final Class<T> from,
            @Nonnull final Class<?>... parameters
    ) {
        try {
            return requireNonNull(from).getConstructor(requireNonNull(parameters));
        } catch (final NoSuchMethodException e) {
            throw new MethodNotFoundException(from, "constructor", parameters, e);
        }
    }

    public static <T> ConstructorDependency<T> constructor(
            @Nonnull final ServiceReference<T> from,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return constructor(
                ConstructorDependency.findConstructor(
                        requireNonNull(from).getType(),
                        toTypes(parameters)
                ),
                parameters
        );
    }
}
