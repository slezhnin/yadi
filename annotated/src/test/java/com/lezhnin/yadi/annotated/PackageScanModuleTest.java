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
        assertThat(dactual.getA()).isNotNull();
        assertThat(dactual.getA().getB()).isNotNull();
        assertThat(dactual.getA().getC()).isNotNull();
        assertThat(dactual.getA().getC().getB()).isNotNull();
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

    public static class D {

        private final A a;

        public D(final A a) {
            this.a = a;
        }

        public A getA() {
            return a;
        }
    }

    @Named
    public static class ModuleD {

        @Named
        public D getD(final A a) {
            return new D(a);
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