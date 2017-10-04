package com.lezhnin.yadi.api;

import static com.lezhnin.yadi.api.ServiceReference.fromTypes;
import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Objects.requireNonNull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public interface Dependency {

    @Nonnull
    ServiceReference<?>[] getReferences();

    interface ImmediateDependency<T> extends Dependency {

        @Nonnull
        Supplier<T> getSupplier();

        static <T> ImmediateDependency<T> immediate(@Nonnull final Supplier<T> supplier) {
            return new ImmediateDependency<T>() {
                @Nonnull
                @Override
                public ServiceReference<?>[] getReferences() {
                    return new ServiceReference<?>[0];
                }

                @Nonnull
                @Override
                public Supplier<T> getSupplier() {
                    return requireNonNull(supplier);
                }
            };
        }
    }

    interface MethodDependency extends Dependency {

        @Nonnull
        Method getMethod();

        static MethodDependency methodFromClass(@Nonnull final Method method) {
            return methodFromClass(
                    requireNonNull(method).getDeclaringClass(),
                    method.getName(),
                    fromTypes(method.getParameterTypes())
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

        static MethodDependency methodFromClass(
                @Nonnull final Class<?> from,
                @Nonnull final String name,
                @Nonnull final ServiceReference<?>... parameters
        ) {
            return new MethodDependency() {
                @Nonnull
                @Override
                public Method getMethod() {
                    return MethodDependency.findMethodInClass(requireNonNull(from), requireNonNull(name), toTypes(parameters));
                }

                @Nonnull
                @Override
                public ServiceReference<?>[] getReferences() {
                    return requireNonNull(parameters);
                }
            };
        }
    }

    interface InstanceMethodDependency<T> extends MethodDependency {

        @Nonnull
        T getInstance();

        static <T> InstanceMethodDependency<T> methodFromInstance(
                @Nonnull final T instance,
                @Nonnull final Method method
        ) {
            return methodFromInstance(
                    requireNonNull(instance),
                    method.getName(),
                    fromTypes(method.getParameterTypes())
            );
        }

        static <T> InstanceMethodDependency<T> methodFromInstance(
                @Nonnull final T instance,
                @Nonnull final String name,
                @Nonnull final ServiceReference<?>... parameters
        ) {
            return new InstanceMethodDependency<T>() {
                @Nonnull
                @Override
                public T getInstance() {
                    return requireNonNull(instance);
                }

                @Nonnull
                @Override
                public Method getMethod() {
                    return MethodDependency.findMethodInClass(requireNonNull(instance).getClass(), requireNonNull(name), toTypes(parameters));
                }

                @Nonnull
                @Override
                public ServiceReference<?>[] getReferences() {
                    return requireNonNull(parameters);
                }
            };
        }
    }

    interface ConstructorDependency<T> extends Dependency {

        Constructor<T> getConstructor();

        static <T> ConstructorDependency<T> constructor(@Nonnull final Constructor<T> constructor) {
            return constructor(
                    requireNonNull(constructor).getDeclaringClass(),
                    fromTypes(constructor.getParameterTypes())
            );
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
                @Nonnull final Class<T> from,
                @Nonnull final ServiceReference<?>... parameters
        ) {
            return new ConstructorDependency<T>() {
                @Nonnull
                @Override
                public Constructor<T> getConstructor() {
                    return ConstructorDependency.findConstructor(requireNonNull(from), toTypes(parameters));
                }

                @Nonnull
                @Override
                public ServiceReference<?>[] getReferences() {
                    return requireNonNull(parameters);
                }
            };
        }
    }
}
