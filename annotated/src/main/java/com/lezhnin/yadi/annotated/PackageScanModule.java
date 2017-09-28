package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.simple.SimpleServiceProvider.provider;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.simple.SimpleModule;
import java.util.Set;
import javax.annotation.Nonnull;
import org.reflections.Reflections;

public class PackageScanModule extends SimpleModule {

    private final String packagePrefix;

    protected PackageScanModule(@Nonnull final String packagePrefix, @Nonnull ServiceLocator... parents) {
        super(parents);
        this.packagePrefix = requireNonNull(packagePrefix);
    }

    public static ServiceLocator fromPackage(@Nonnull String prefix, @Nonnull ServiceLocator... parents) {
        return new PackageScanModule(prefix, parents);
    }

    public static ServiceLocator fromPackage(@Nonnull Package pkg, @Nonnull ServiceLocator... parents) {
        return new PackageScanModule(pkg.getName(), parents);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doBind() {
        final Reflections reflections = new Reflections(packagePrefix);
        final Set<Class<?>> annotatedServiceClasses = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> annotatedServiceClass : annotatedServiceClasses) {
            final Service serviceAnnotation = requireNonNull(annotatedServiceClass.getAnnotation(Service.class));
            final Class<?>[] implementedServiceInterfaces = (
                    serviceAnnotation.implemented().length != 0 ?
                            serviceAnnotation.implemented() : (
                            annotatedServiceClass.getInterfaces().length != 0 ?
                                    annotatedServiceClass.getInterfaces() :
                                    new Class<?>[]{annotatedServiceClass}
                    )
            );
            for (Class implementedServiceInterface : implementedServiceInterfaces) {
                bind(implementedServiceInterface).to(provider(annotatedServiceClass, serviceAnnotation.dependencies()));
            }
        }
    }
}
