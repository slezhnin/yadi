package com.lezhnin.yadi.api.dependency;

import com.lezhnin.yadi.api.ServiceReference;
import javax.annotation.Nonnull;

public interface Dependency<T> {

    @Nonnull
    ServiceReference<T> getTargetReference();

    @Nonnull
    ServiceReference<?>[] getReferences();
}
