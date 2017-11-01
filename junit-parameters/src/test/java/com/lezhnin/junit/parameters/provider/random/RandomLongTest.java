package com.lezhnin.junit.parameters.provider.random;

import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.junit.parameters.AnnotatedArgumentSource;
import com.lezhnin.junit.parameters.Limit;
import com.lezhnin.junit.parameters.Parameters;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

class RandomLongTest {

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomLong.class)
    void testRandomLong(final Long randomLong) {
        assertThat(randomLong).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomLong.class)
    void testRandomLongPositive(@Limit("positive") final Long randomLong) {
        assertThat(randomLong).isNotNull();
        assertThat(randomLong).isGreaterThanOrEqualTo(0);
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomLong.class)
    void testRandomLongNegative(@Limit("negative") final Long randomLong) {
        assertThat(randomLong).isNotNull();
        assertThat(randomLong).isLessThan(0);
    }
}