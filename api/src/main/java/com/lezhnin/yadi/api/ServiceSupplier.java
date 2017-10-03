package com.lezhnin.yadi.api;

import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public interface ServiceSupplier<T> extends Supplier<T> {

    ServiceReference<T> getReference();

    ServiceSupplier<?>[] getSuppliers();

    abstract class ReferenceSupplier<T> implements ServiceSupplier<T> {

        final private ServiceReference<T> reference;
        final private ServiceSupplier<?>[] suppliers;

        public ReferenceSupplier(@Nonnull final ServiceReference<T> reference,
                                 @Nonnull final ServiceSupplier<?>... suppliers) {
            this.reference = requireNonNull(reference);
            stream(requireNonNull(suppliers)).forEach(Objects::requireNonNull);
            this.suppliers = suppliers;
        }

        @Override
        public ServiceReference<T> getReference() {
            return reference;
        }

        @Override
        public ServiceSupplier<?>[] getSuppliers() {
            return suppliers;
        }
    }

    static <T> ServiceSupplier<T> immediateSupplier(@Nonnull final Class<T> immediateType,
                                                    @Nonnull final Supplier<T> supplier) {
        return new ReferenceSupplier<T>(serviceReference(immediateType, null)) {

            @Override
            public T get() {
                return supplier.get();
            }
        };
    }

    static <T> ServiceSupplier<T> serviceSupplier(@Nonnull final ServiceReference<T> serviceReference,
                                                  @Nonnull final ServiceLocator serviceLocator,
                                                  @Nonnull final ServiceSupplier<T> instanceSupplier,
                                                  @Nonnull final ServiceSupplier<?>... postSuppliers) {
        return new ReferenceSupplier<T>(requireNonNull(serviceReference), requireNonNull(postSuppliers)) {

            @Override
            public T get() {
                return instanceSupplier.get();
            }
        };
    }
}
