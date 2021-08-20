package org.team4u.selector.domain.selector;

import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 选择执行器工厂服务
 *
 * @author jay.wu
 */
public class SelectorFactoryService extends PolicyRegistrar<String, SelectorFactory> {

    public SelectorFactoryService() {
        registerByBeanProvidersAndEvent();
    }
}