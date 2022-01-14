package org.team4u.workflow.infrastructure.persistence.instance.ext;

import org.team4u.base.lang.lazy.LazySupplier;
import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 扩展处理器服务
 *
 * @author jay.wu
 */
public class ExtTypeHandlerHolder extends PolicyRegistrar<Void, ExtTypeHandler> {

    private static final LazySupplier<ExtTypeHandlerHolder> instance = LazySupplier.of(ExtTypeHandlerHolder::new);

    public static ExtTypeHandlerHolder getInstance() {
        return instance.get();
    }

    public ExtTypeHandlerHolder() {
        registerPolicy(new DefaultStringExtTypeHandler());
        registerPoliciesByBeanProvidersAndEvent();
    }

    public ExtTypeHandler handler() {
        return availablePolicyOf(null);
    }
}