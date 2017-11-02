package com.lezhnin.yadi.api.dependency;

import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceReference;
import com.lezhnin.yadi.api.exception.MethodNotFoundException;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NonNull;

@Data
public class MethodDependency<T> implements Dependency {

    @NonNull
    private final ServiceReference<T> reference;

    @NonNull
    private final Method method;

    @NonNull
    private final ServiceReference<?>[] parameters;

    public static <T> MethodDependency<T> methodFromClass(
            @Nonnull final ServiceReference<T> reference,
            @Nonnull final Method method,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return new MethodDependency<>(reference, method, parameters);
    }

    public static <T> MethodDependency<T> methodFromClass(
            @Nonnull final ServiceReference<T> reference,
            @Nonnull final String name,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return methodFromClass(
                reference,
                MethodDependency.findMethodInClass(
                        requireNonNull(reference).getType(),
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
            return requireNonNull(from).getMethod(name, parameters);
        } catch (final NoSuchMethodException e) {
            throw new MethodNotFoundException(from, name, parameters, e);
        }
    }
}
