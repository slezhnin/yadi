package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.simple.SimpleServiceBeanProvider.provider;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceBeanLocator;
import com.lezhnin.yadi.simple.SimpleModule;
import java.util.Set;
import javax.annotation.Nonnull;
import org.reflections.Reflections;

public class PackageScanModule extends SimpleModule {

    private final String packagePrefix;

    protected PackageScanModule(@Nonnull final String packagePrefix, @Nonnull ServiceBeanLocator... parents) {
        super(parents);
        this.packagePrefix = requireNonNull(packagePrefix);
    }

    public static ServiceBeanLocator fromPackage(@Nonnull String prefix, @Nonnull ServiceBeanLocator... parents) {
        return new PackageScanModule(prefix, parents);
    }

    public static ServiceBeanLocator fromPackage(@Nonnull Package pkg, @Nonnull ServiceBeanLocator... parents) {
        return new PackageScanModule(pkg.getName(), parents);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doBind() {
        final Reflections reflections = new Reflections(packagePrefix);
        final Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(ServiceBean.class);
        for (Class<?> annotatedClass : annotatedClasses) {
            final ServiceBean annotation = requireNonNull(annotatedClass.getAnnotation(ServiceBean.class));
            final Class<?>[] implementedInterfaces = (
                    annotation.implemented().length != 0 ?
                            annotation.implemented() : (
                            annotatedClass.getInterfaces().length != 0 ?
                                    annotatedClass.getInterfaces() :
                                    new Class<?>[]{annotatedClass}
                    )
            );
            for (Class implementedInterface : implementedInterfaces) {
                bind(implementedInterface).to(provider(annotatedClass, annotation.dependencies()));
            }
        }
    }
}
