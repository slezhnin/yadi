package com.lezhnin.yadi.api;

import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Objects.requireNonNull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

public interface Dependency<T> {

    @Nonnull
    ServiceReference<T> getReference();

    @Nonnull
    ServiceReference<?>[] getReferences();

    interface MethodDependency<T> extends Dependency<T> {

        @Nonnull
        Method getMethod();

        static <T> MethodDependency methodFromClass(@Nonnull final ServiceReference<T> reference,
                                                    @Nonnull final Method method,
                                                    @Nonnull final ServiceReference<?>... parameters
        ) {
            return new MethodDependency() {
                @Nonnull
                @Override
                public ServiceReference getReference() {
                    return requireNonNull(reference);
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

        static <T> MethodDependency methodFromClass(
                @Nonnull final ServiceReference<T> from,
                @Nonnull final String name,
                @Nonnull final ServiceReference<?>... parameters
        ) {
            return methodFromClass(
                    requireNonNull(from),
                    MethodDependency.findMethodInClass(
                            from.getType(),
                            requireNonNull(name),
                            toTypes(requireNonNull(parameters))
                    ),
                    parameters
            );
        }
    }

    interface InstanceMethodDependency<T> extends MethodDependency<T> {

        @Nonnull
        T getInstance();

        static <T> InstanceMethodDependency<T> methodFromInstance(
                @Nonnull final T instance,
                @Nonnull final Method method,
                @Nonnull final ServiceReference<?>... parameters
        ) {
            return new InstanceMethodDependency<T>() {
                @Nonnull
                @Override
                public T getInstance() {
                    return requireNonNull(instance);
                }

                @SuppressWarnings("unchecked")
                @Nonnull
                @Override
                public ServiceReference<T> getReference() {
                    return serviceReference((Class<T>) requireNonNull(instance).getClass());
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

        static <T> InstanceMethodDependency<T> methodFromInstance(
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

    interface ConstructorDependency<T> extends Dependency<T> {

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
                public ServiceReference<T> getReference() {
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
}
