package com.lezhnin.junit.parameters;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.junit.jupiter.params.provider.Arguments;

public class ArgumentsFactory implements Supplier<Arguments> {

    private final Parameters parameters;
    private final Class<?>[] parameterTypes;

    ArgumentsFactory(final Parameters parameters, final Class<?>[] parameterTypes) {
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
                        new ArgumentFactory(parameters.value()[i], parameterTypes[i], parameters.maxSize()).get()
                ).toArray()
        );
    }
}
