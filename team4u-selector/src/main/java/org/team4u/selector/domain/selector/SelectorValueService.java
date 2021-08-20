package org.team4u.selector.domain.selector;

import org.team4u.base.registrar.PolicyRegistrar;

import java.util.List;

/**
 * 选择器值服务
 *
 * @author jay.wu
 */
public class SelectorValueService extends PolicyRegistrar<String, SelectorValueHandler> {

    public SelectorValueService() {
        registerPoliciesByBeanProvidersAndEvent();
    }

    public SelectorValueService(List<SelectorValueHandler> objects) {
        super(objects);
    }
}