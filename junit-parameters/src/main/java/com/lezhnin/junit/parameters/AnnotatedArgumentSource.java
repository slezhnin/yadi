package com.lezhnin.junit.parameters;

import static java.util.stream.Stream.generate;
import com.lezhnin.junit.parameters.factory.ArgumentsFactory;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class AnnotatedArgumentSource implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final Parameters parameters = context.getRequiredTestMethod().getAnnotation(Parameters.class);
        return Optional.of(parameters)
                       .map(pa -> argumentStream(pa, context.getRequiredTestMethod().getParameterTypes()))
                       .orElseThrow(() -> new RuntimeException("No @Parameters was specified"));
    }

    private Stream<? extends Arguments> argumentStream(final Parameters parameters, final Class<?>[] parameterTypes) {
        return generate(new ArgumentsFactory(parameters, parameterTypes)).limit(parameters.count());
    }
}
