package com.lezhnin.junit.parameters;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.generate;
import java.lang.reflect.Array;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class AnnotatedArgumentSource implements ArgumentsProvider {

    private static final Random random = new Random();

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final Parameters parameters = context.getRequiredTestMethod().getAnnotation(Parameters.class);
        return Optional.of(parameters).map(pa -> generate(() -> {
                    if (pa.value().length != context.getRequiredTestMethod().getParameterTypes().length) {
                        throw new RuntimeException("Invalid @Parameters.value() length is specified");
                    }
                    final Object[] args = new Object[pa.value().length];
                    for (int i = 0; i < pa.value().length; i++) {
                        args[i] = argumentSupplier(
                                pa.value()[i],
                                context.getRequiredTestMethod().getParameterTypes()[i],
                                pa.count()
                        );
                    }
                    return Arguments.of(args);
                }
        ).limit(pa.count())).orElseThrow(() ->
                new RuntimeException("No @Parameters was specified")
        );
    }

    private Object argumentSupplier(final Class<? extends Supplier<?>> supplierType, final Class<?> parameterType, final int count) {
        try {
            final Supplier<?> supplier = supplierType.newInstance();
            if (parameterType.getName().startsWith("[")) {
                return generate(supplier)
                        .limit(random.nextInt(count))
                        .toArray(length -> (Object[]) Array.newInstance(supplier.get().getClass(), length));
            } else if (parameterType.isAssignableFrom(Set.class)) {
                return generate(supplier)
                        .limit(random.nextInt(count))
                        .collect(toSet());
            } else if (parameterType.isAssignableFrom(Iterable.class)) {
                return generate(supplier)
                        .limit(random.nextInt(count))
                        .collect(toList());
            } else {
                return supplier.get();
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
