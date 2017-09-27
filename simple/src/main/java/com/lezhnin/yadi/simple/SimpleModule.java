package com.lezhnin.yadi.simple;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.ServiceBeanLocator;
import com.lezhnin.yadi.api.ServiceBeanStorage;
import javax.annotation.Nonnull;

public class SimpleModule extends SimpleServiceBeanModule {

    private SimpleModule(@Nonnull ServiceBeanStorage storage, @Nonnull ServiceBeanLocator... parents) {
        super(
                new SimpleServiceBeanBinder(requireNonNull(storage)),
                new SimpleServiceBeanProviderFinder(requireNonNull(storage)),
                asList(requireNonNull(parents))
        );
        doBind();
    }

    protected SimpleModule(@Nonnull ServiceBeanLocator... parents) {
        this(new SimpleServiceBeanStorage(), parents);
    }

    @Nonnull
    public static SimpleServiceBeanModule simpleModule(@Nonnull ServiceBeanLocator... parents) {
        return new SimpleModule(parents);
    }

    protected void doBind() {}
}
