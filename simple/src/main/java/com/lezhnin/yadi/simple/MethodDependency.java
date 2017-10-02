package com.lezhnin.yadi.simple;

import static java.util.Objects.requireNonNull;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

public class MethodDependency extends ServiceDependency {

    private final Method method;

    private MethodDependency(@Nonnull final Method method, @Nonnull final Class<?>[] dependencies) {
        super(dependencies);
        this.method = requireNonNull(method);
    }

    @Nonnull
    public Method getMethod() {
        return method;
    }

    public static ServiceDependency method(@Nonnull final Method method, @Nonnull final Class<?>... dependencies) {
        return new MethodDependency(method, dependencies);
    }

    public static ServiceDependency method(@Nonnull final Class<?> dependency,
                                           @Nonnull final String method,
                                           @Nonnull final Class<?>... dependencies) {
        try {
            return new MethodDependency(dependency.getMethod(method, dependencies), dependencies);
        } catch (NoSuchMethodException e) {
            throw new MethodDependencyException(dependency, method, dependencies, e);
        }
    }
}
