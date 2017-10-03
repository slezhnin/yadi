package com.lezhnin.yadi.simple;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import java.lang.reflect.Method;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MethodDependency extends ServiceDependency {

    private final Method method;
    private final Object instance;

    private MethodDependency(@Nonnull final Method method, @Nullable final Object instance) {
        super(requireNonNull(method).getParameterTypes());
        this.method = method;
        this.instance = instance;
    }

    @Nullable
    public Object getInstance() {
        return instance;
    }

    @Nonnull
    public Method getMethod() {
        return method;
    }

    public static ServiceDependency method(@Nonnull final Method method, @Nullable final Object instance) {
        return new MethodDependency(method, instance);
    }

    public static ServiceDependency method(@Nonnull final Class<?> dependency,
                                           @Nonnull final String method,
                                           @Nullable final Object instance,
                                           @Nonnull final Class<?>... dependencies) {
        return new MethodDependency(methodOf(dependency, method, dependencies), instance);
    }

    public static Method methodOf(@Nonnull final Object typeOrInstance,
                                  @Nonnull final String method,
                                  @Nonnull final Class<?>... parameters) {
        final Class<?> type = typeFromInstance(requireNonNull(typeOrInstance));
        try {
            return type.getMethod(requireNonNull(method), requireNonNull(parameters));
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException(type, method, parameters, e);
        }
    }

    public static Method firstMethodOf(@Nonnull final Object typeOrInstance, @Nonnull final String method) {
        final Class<?> type = typeFromInstance(requireNonNull(typeOrInstance));
        return stream(type.getMethods())
                .filter(m -> Objects.equals(m.getName(), method))
                .findFirst()
                .orElseThrow(() -> new MethodNotFoundException(type, method, new Class[0]));
    }

    private static Class<?> typeFromInstance(Object typeOrInstance) {
        if (typeOrInstance instanceof Class) {
            return (Class<?>) typeOrInstance;
        }
        return typeOrInstance.getClass();
    }
}
