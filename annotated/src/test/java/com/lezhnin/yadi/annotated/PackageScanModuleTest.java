package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.PackageScanModule.fromPackage;
import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.yadi.api.ServiceLocator;
import javax.inject.Inject;
import javax.inject.Named;
import org.junit.jupiter.api.Test;

class PackageScanModuleTest {

    @Test
    void testFromPackage() {
        final ServiceLocator module = fromPackage(getClass().getPackage());

        final A actual = module.locate(serviceReference(A.class, "blah!")).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getB()).isNotNull();
        assertThat(actual.getC()).isNotNull();
        assertThat(actual.getC().getB()).isNotNull();

        final D dactual = module.locate(serviceReference(D.class)).get();

        assertThat(dactual).isNotNull();
        assertThat(dactual.getE()).isNotNull();
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