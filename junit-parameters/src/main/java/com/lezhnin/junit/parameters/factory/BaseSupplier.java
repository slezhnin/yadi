package com.lezhnin.junit.parameters.factory;

import com.lezhnin.junit.parameters.provider.ValueProvider;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

abstract class BaseSupplier<T, R> implements Supplier<R> {

    private static final Random random = new Random();

    private final ValueProvider<T> provider;
    private final Class<?> parameterType;
    private final int maxSize;

    BaseSupplier(final ValueProvider<T> provider, final Class<?> parameterType, final int maxSize) {
        this.provider = provider;
        this.parameterType = parameterType;
        this.maxSize = maxSize;
    }

    public ValueProvider<T> getProvider() {
        return provider;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public int getMaxSize() {
        return maxSize;
    }

    protected Stream<T> generateFromProvider() {
        return Stream.generate(() -> provider.apply(parameterType))
                     .limit(random.nextInt(maxSize));
    }

    static Random getRandom() {
        return random;
    }
}
