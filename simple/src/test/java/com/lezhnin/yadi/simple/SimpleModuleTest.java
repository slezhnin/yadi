package com.lezhnin.yadi.simple;

import static com.lezhnin.yadi.simple.ConstructorDependency.constructor;
import static com.lezhnin.yadi.simple.MethodDependency.method;
import static com.lezhnin.yadi.simple.MethodDependency.methodOf;
import static com.lezhnin.yadi.simple.SimpleModule.simpleModule;
import static com.lezhnin.yadi.simple.SimpleServiceProvider.provider;
import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.yadi.api.ServiceLocator;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SimpleModuleTest {

    @Test
    void test1() {
        final ServiceLocator parent = new SimpleModule() {
            @Override
            protected void doBind() {
                bind(B.class).to(provider(B.class));
            }
        };

        class Module extends SimpleModule {

            public Module() {
                super(parent);
            }

            public A createA(final B b, final C c) {
                return new A(b, c);
            }

            @Override
            protected void doBind() {
                // bind(A.class).to(provider(A.class, constructor(B.class, C.class)));
                bind(A.class).to(provider(
                        A.class, Module.this,
                        methodOf(Module.this, "createA", B.class, C.class)
                ));
                bind(C.class).to(provider(C.class, method(C.class, "setB", null, B.class)));
            }
        };

        final ServiceLocator module = new Module();

        final A actual = module.locate(A.class).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getB()).isNotNull();
        assertThat(actual.getC()).isNotNull();
    }

    @Test
    void test2() {
        final SimpleModule parent = simpleModule();
        parent.bind(B.class).to(provider(B.class));

        final SimpleModule module = simpleModule(parent);
        module.bind(A.class).to(provider(A.class, constructor(B.class, C.class)))
              .bind(C.class).to(provider(C.class, method(C.class, "setB", null, B.class)));

        final A actual = module.locate(A.class).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getB()).isNotNull();
        assertThat(actual.getC()).isNotNull();
        assertThat(actual.getC().getB()).isNotNull();
    }

    @Disabled
    @Test
    void test3() {
        final ServiceLocator module = new SimpleModule() {
            @Override
            protected void doBind() {
                bind(A.class).to(provider(A.class, constructor(B.class, C.class)));
                bind(B.class).to(provider(B.class, method(B.class, "setA", null, A.class)));
                bind(C.class).to(provider(C.class, method(C.class, "setB", null, B.class)));
            }
        };

        final A actual = module.locate(A.class).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getB()).isNotNull();
        assertThat(actual.getB().getA()).isNotNull();
        assertThat(actual.getC()).isNotNull();
        assertThat(actual.getC().getB()).isNotNull();
    }

    public static class Amodule {

        public A createA(final B b, final C c) {
            return new A(b, c);
        }
    }

    public static class B {

        private A a;

        public A getA() {
            return a;
        }

        public void setA(final A a) {
            this.a = a;
        }
    }

    public static class C {

        private B b;

        public B getB() {
            return b;
        }

        public void setB(final B b) {
            this.b = b;
        }
    }

    public static class A {

        private final B b;
        private final C c;

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