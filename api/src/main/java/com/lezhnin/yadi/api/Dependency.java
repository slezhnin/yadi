package com.lezhnin.yadi.api;

import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static com.lezhnin.yadi.api.ServiceReference.toTypes;
import static java.util.Objects.requireNonNull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

public interface Dependency<T> {

    @Nonnull
    ServiceReference<T> getTargetReference();

    @Nonnull
    ServiceReference<?>[] getReferences();

    interface MethodDependency<F, T> extends Dependency<T> {

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
    }

    interface InstanceMethodDependency<F, T> extends MethodDependency<F, T> {

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
}
