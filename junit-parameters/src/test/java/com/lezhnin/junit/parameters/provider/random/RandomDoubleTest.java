package com.lezhnin.junit.parameters.provider.random;

import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.junit.parameters.AnnotatedArgumentSource;
import com.lezhnin.junit.parameters.Limit;
import com.lezhnin.junit.parameters.Parameters;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

class RandomDoubleTest {

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomDouble.class)
    void testRandomDouble(final Double randomDouble) {
        assertThat(randomDouble).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomDouble.class)
    void testRandomDoublePositive(@Limit("positive") final Double randomDouble) {
        assertThat(randomDouble).isNotNull();
        assertThat(randomDouble).isGreaterThanOrEqualTo(0);
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomDouble.class)
    void testRandomDoubleNegative(@Limit("negative") final Double randomDouble) {
        assertThat(randomDouble).isNotNull();
        assertThat(randomDouble).isLessThan(0);
    }
}