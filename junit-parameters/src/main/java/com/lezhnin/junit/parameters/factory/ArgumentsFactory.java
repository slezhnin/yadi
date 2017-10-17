package com.lezhnin.junit.parameters.factory;

import com.lezhnin.junit.parameters.Parameters;
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

    private <T> T instance(final Class<T> type) {
        try {
            return type.newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Arguments get() {
        return Arguments.of(
                IntStream.range(0, parameters.value().length).mapToObj(i ->
                        new ArgumentSupplier(instance(parameters.value()[i]), parameterTypes[i], parameters.maxSize()).get()
                ).toArray()
        );
    }
}
