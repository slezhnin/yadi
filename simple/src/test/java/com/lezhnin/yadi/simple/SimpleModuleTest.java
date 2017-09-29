package com.lezhnin.yadi.simple;

import static com.lezhnin.yadi.simple.ConstructorDependency.constructor;
import static com.lezhnin.yadi.simple.MethodDependency.method;
import static com.lezhnin.yadi.simple.SimpleModule.simpleModule;
import static com.lezhnin.yadi.simple.SimpleServiceProvider.provider;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.lezhnin.yadi.api.ServiceLocator;
import org.junit.jupiter.api.Test;

class SimpleModuleTest {

    public static class B {

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

    @Test
    void test1() {
        final ServiceLocator parent = new SimpleModule() {
            @Override
            protected void doBind() {
                bind(B.class).to(provider(B.class));
            }
        };

        final ServiceLocator module = new SimpleModule(parent) {
            @Override
            protected void doBind() {
                bind(A.class).to(provider(A.class, constructor(B.class, C.class)));
                bind(C.class).to(provider(C.class, method(C.class, "setB", B.class)));
            }
        };

        final A actual = module.locate(A.class);

        assertNotNull(actual);
        assertNotNull(actual.getB());
        assertNotNull(actual.getC());
    }

    @Test
    void test2() {
        final SimpleModule parent = simpleModule();
        parent.bind(B.class).to(provider(B.class));

        final SimpleModule module = simpleModule(parent);
        module.bind(A.class).to(provider(A.class, constructor(B.class, C.class)))
              .bind(C.class).to(provider(C.class));

        final A actual = module.locate(A.class);

        assertNotNull(actual);
        assertNotNull(actual.getB());
        assertNotNull(actual.getC());
    }
}