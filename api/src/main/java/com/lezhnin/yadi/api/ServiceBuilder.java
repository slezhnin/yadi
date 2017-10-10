package com.lezhnin.yadi.api;

import static com.lezhnin.yadi.api.Dependency.ConstructorDependency.constructor;
import static com.lezhnin.yadi.api.Dependency.MethodDependency.methodFromClass;
import static com.lezhnin.yadi.api.ServiceDefinition.serviceDefinition;
import static com.lezhnin.yadi.api.ServiceReference.fromTypes;
import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static java.util.Arrays.asList;
import java.util.ArrayList;
import java.util.List;

public interface ServiceBuilder<T> {

    WithReference<T> forClass(Class<? extends T> serviceClass);

    static <T> ServiceBuilder<T> service() {
        return new ServiceBuilder<T>() {

            @Override
            public WithReference<T> forClass(final Class<? extends T> serviceClass) {
                return forReference(serviceReference(serviceClass));
            }

            public WithReference<T> forReference(final ServiceReference<? extends T> serviceReference) {
                return withReference(serviceReference);
            }

            private WithReference<T> withReference(final ServiceReference<? extends T> serviceReference) {
                return new WithReference<T>() {
                    @Override
                    public WithReference<T> withName(final String name) {
                        return withReference(serviceReference(serviceReference.getType(), name));
                    }

                    @Override
                    public ForConstructor<T> forConstructor() {
                        return new ForConstructor<T>() {
                            @Override
                            public PostConstruct<T> postConstruct() {
                                return postConstructWithDefinition(build());
                            }

                            @Override
                            public ServiceDefinition<T> build() {
                                return serviceDefinition(serviceReference, constructor(serviceReference));
                            }

                            @Override
                            public WithParameters<T> withParameters(final ServiceReference<?>... parameters) {
                                return new WithParameters<T>() {
                                    @Override
                                    public PostConstruct<T> postConstruct() {
                                        return postConstructWithDefinition(build());
                                    }

                                    @Override
                                    public ServiceDefinition<T> build() {
                                        return serviceDefinition(serviceReference, constructor(serviceReference, parameters));
                                    }
                                };
                            }
                        };
                    }

                    @Override
                    public ForMethod<T> forMethod() {
                        return name -> new ForMethod.MethodWithName<T>() {
                            @Override
                            public ForMethod.InClass<T> inClass(final ServiceReference<?> classReference) {
                                return new ForMethod.InClass<T>() {

                                    @Override
                                    public PostConstruct postConstruct() {
                                        return postConstructWithDefinition(build());
                                    }

                                    @Override
                                    public ServiceDefinition<T> build() {
                                        return serviceDefinition(
                                                serviceReference,
                                                methodFromClass(classReference, serviceReference, name)
                                        );
                                    }
                                };
                            }

                            @Override
                            public PostConstruct<T> postConstruct() {
                                return postConstructWithDefinition(build());
                            }

                            @Override
                            public ServiceDefinition<T> build() {
                                return serviceDefinition(serviceReference, methodFromClass(serviceReference, name));
                            }

                            @Override
                            public WithParameters<T> withParameters(final ServiceReference<?>... parameters) {
                                return new WithParameters<T>() {
                                    @Override
                                    public ForMethod.InClass<T> inClass(final ServiceReference<?> classReference) {
                                        return new ForMethod.InClass<T>() {

                                            @Override
                                            public PostConstruct postConstruct() {
                                                return postConstructWithDefinition(build());
                                            }

                                            @Override
                                            public ServiceDefinition<T> build() {
                                                return serviceDefinition(
                                                        serviceReference,
                                                        methodFromClass(classReference, serviceReference, name, parameters)
                                                );
                                            }
                                        };
                                    }

                                    @Override
                                    public PostConstruct<T> postConstruct() {
                                        return postConstructWithDefinition(build());
                                    }

                                    @Override
                                    public ServiceDefinition<T> build() {
                                        return serviceDefinition(serviceReference, methodFromClass(serviceReference, name, parameters));
                                    }
                                };
                            }
                        };
                    }

                    PostConstruct<T> postConstructWithDefinition(final ServiceDefinition<T> serviceDefinition) {
                        return new PostConstruct<T>() {
                            @Override
                            public PostMethod<T> method() {
                                return methodName -> new PostMethod.MethodWithName<T>() {
                                    @Override
                                    public ServiceDefinition<T> build() {
                                        return addMethod(
                                                serviceDefinition,
                                                methodFromClass(serviceReference, methodName)
                                        );
                                    }

                                    @Override
                                    public PostMethod<T> method() {
                                        return postConstructWithDefinition(build()).method();
                                    }

                                    @Override
                                    public WithParameters<T> withParameters(final ServiceReference<?>... parameters) {
                                        return new WithParameters<T>() {
                                            @Override
                                            public ServiceDefinition<T> build() {
                                                return addMethod(
                                                        serviceDefinition,
                                                        methodFromClass(serviceReference, methodName, parameters)
                                                );
                                            }

                                            @Override
                                            public PostMethod<T> method() {
                                                return postConstructWithDefinition(build()).method();
                                            }
                                        };
                                    }
                                };
                            }

                            @Override
                            public ServiceDefinition<T> build() {
                                return serviceDefinition;
                            }

                            private ServiceDefinition<T> addMethod(final ServiceDefinition<T> serviceDefinition, final Dependency
                                    method) {
                                final List<Dependency> dependencies = new ArrayList<>(
                                        asList(serviceDefinition.getPostConstructionDependencies())
                                );
                                dependencies.add(method);
                                return serviceDefinition(
                                        serviceDefinition.getReference(),
                                        serviceDefinition.getConstructionDependency(),
                                        dependencies.toArray(new Dependency[dependencies.size()])
                                );
                            }
                        };
                    }
                };
            }
        };
    }

    interface FinishBuild<T> {

        ServiceDefinition<T> build();
    }

    interface WithReference<T> {

        WithReference<T> withName(String name);

        ForConstructor<T> forConstructor();

        interface WithPostConstruct<T> {

            PostConstruct<T> postConstruct();
        }

        interface ForConstructor<T> extends FinishBuild<T>, WithPostConstruct<T> {

            interface WithParameters<T> extends FinishBuild<T>, WithPostConstruct<T> {

            }

            WithParameters<T> withParameters(ServiceReference<?>... parameters);

            default WithParameters<T> withParameters(final Class<?>... parameters) {
                return withParameters(fromTypes(parameters));
            }
        }

        ForMethod<T> forMethod();

        interface ForMethod<T> {

            MethodWithName<T> withName(String name);

            interface MethodWithName<T> extends ForClass<T>, FinishBuild<T>, WithPostConstruct<T> {

                WithParameters<T> withParameters(ServiceReference<?>... parameters);

                default WithParameters<T> withParameters(final Class<?>... parameters) {
                    return withParameters(fromTypes(parameters));
                }

                interface WithParameters<T> extends ForClass<T>, FinishBuild<T>, WithPostConstruct<T> {

                }
            }

            interface ForClass<T> {

                InClass<T> inClass(ServiceReference<?> classReference);

                default InClass<T> inClass(final Class<?> classReference) {
                    return inClass(serviceReference(classReference));
                }
            }

            interface InClass<T> extends FinishBuild<T>, WithPostConstruct {

            }
        }

        interface PostConstruct<T> extends FinishBuild<T> {

            PostMethod<T> method();

            interface PostMethod<T> {

                MethodWithName<T> withName(String name);

                interface MethodWithName<T> extends FinishBuild<T> {

                    PostMethod<T> method();

                    WithParameters<T> withParameters(ServiceReference<?>... parameters);

                    default WithParameters<T> withParameters(final Class<?>... parameters) {
                        return withParameters(fromTypes(parameters));
                    }

                    interface WithParameters<T> extends FinishBuild<T> {

                        PostMethod<T> method();
                    }
                }
            }
        }
    }
}
