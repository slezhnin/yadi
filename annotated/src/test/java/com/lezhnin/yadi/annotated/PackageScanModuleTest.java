package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.PackageScanModule.fromPackage;
import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.yadi.api.ServiceLocator;
import javax.inject.Inject;
import javax.inject.Named;

import com.lezhnin.yadi.api.ServiceReference;
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
        public void setB(final B b) {
            this.b = b;
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