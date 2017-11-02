package com.lezhnin.yadi.api.dependency;

import com.lezhnin.yadi.api.ServiceReference;
import javax.annotation.Nonnull;

public interface Dependency {

    @Nonnull
    ServiceReference<?>[] getParameters();
}
