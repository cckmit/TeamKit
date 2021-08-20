package org.team4u.selector.domain.interceptor;

import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 选择执行器工厂服务
 *
 * @author jay.wu
 */
public class SelectorInterceptorFactoryService extends PolicyRegistrar<String, SelectorInterceptorFactory> {

    public SelectorInterceptorFactoryService() {
        registerPoliciesByBeanProvidersAndEvent();
    }
}