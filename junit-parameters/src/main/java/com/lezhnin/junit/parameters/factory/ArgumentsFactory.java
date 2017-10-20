package com.lezhnin.junit.parameters.factory;

import com.lezhnin.junit.parameters.Parameters;
import com.lezhnin.junit.parameters.provider.ValueProvider;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.junit.jupiter.params.provider.Arguments;

public class ArgumentsFactory implements Supplier<Arguments> {

    private final Parameters parameters;
    private final Class<?>[] parameterTypes;

    public ArgumentsFactory(final Parameters parameters, final Class<?>[] parameterTypes) {
        if (parameters.value().length != parameterTypes.length) {
            throw new RuntimeException("Invalid @Parameters.value() length is specified");
        }
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public Arguments get() {
        return Arguments.of(
                IntStream.range(0, parameters.value().length).mapToObj(i ->
                        supplier(parameters.value()[i], parameterTypes[i], parameters.maxSize()).get()
                ).toArray()
        );
    }

    @SuppressWarnings("unchecked")
    private <T> ArgumentSupplier<T> supplier(
            final Class<? extends ValueProvider<?>> providerClass,
            final Class<?> parameterType,
            final int maxSize
    ) {
        try {
            return new ArgumentSupplier<>((ValueProvider<T>) providerClass.newInstance(), parameterType, maxSize);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
