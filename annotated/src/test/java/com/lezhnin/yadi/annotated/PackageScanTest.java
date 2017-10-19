package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.PackageScan.scanPackage;
import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.simple.SimpleModule;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import org.junit.jupiter.api.Test;

class PackageScanTest {

    @Test
    void testFromPackage() {
        final Optional<ServiceLocator> locatorOptional = scanPackage(SimpleModule.module(), getClass().getPackage());

        final Optional<A> oActual = locatorOptional.map(locator ->
                locator.locate(serviceReference(A.class, "blah!")).get()
        );

        assertThat(oActual).isPresent();
        oActual.ifPresent(actual -> assertThat(actual.getB()).isNotNull());
        oActual.ifPresent(actual -> assertThat(actual.getC()).isNotNull());
        oActual.ifPresent(actual -> assertThat(actual.getC().getB()).isNotNull());

        final Optional<D> oDactual = locatorOptional.map(locator ->
                locator.locate(serviceReference(D.class)).get()
        );

        assertThat(oDactual).isPresent();
        oDactual.ifPresent(actual -> assertThat(actual.getE()).isNotNull());
    }

    @Named("bah!")
    public static class B {

    }

    @Named
    public static class C {

        private B b;

        public B getB() {
            return b;
        }

        @Inject
        public void setB(@Named("bah!") final B b) {
            this.b = b;
        }
    }

    public static class D {

        private final E e;

        public D(final E e) {
            this.e = e;
        }

        public E getE() {
            return e;
        }
    }

    @Named("E1")
    public static class E {

    }

    @Named
    public static class ModuleD {

        @Named("E2")
        public E getE2() {
            return new E();
        }

        @Named
        public D getD(@Named("E2") final E e) {
            return new D(e);
        }
    }

    @Named("blah!")
    public static class A {

        private final B b;
        private final C c;

        public A(final B b) {
            this.b = b;
            this.c = null;
        }

        @Inject
        public A(final B b, final C c) {
            this.b = b;
            this.c = c;
        }

        B getB() {
            return b;
        }

        C getC() {
            return c;
        }
    }
}