package com.lezhnin.yadi.api.dependency;

import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceReference;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NonNull;

@Data
public class InstanceMethodDependency<T> implements Dependency {

    @NonNull
    private final T instance;

    @NonNull
    private final Method method;

    @NonNull
    private final ServiceReference<?>[] parameters;

    public static <T> InstanceMethodDependency<T> methodFromInstance(
            @Nonnull final T instance,
            @Nonnull final Method method,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return new InstanceMethodDependency<>(instance, method, parameters);
    }

    public static <T> InstanceMethodDependency<T> methodFromInstance(
            @Nonnull final T instance,
            @Nonnull final String name,
            @Nonnull final ServiceReference<?>... parameters
    ) {
        return methodFromInstance(
                instance,
                MethodDependency.findMethodInClass(
                        requireNonNull(instance).getClass(),
                        requireNonNull(name),
                        toTypes(parameters)),
                parameters
        );
    }
}
