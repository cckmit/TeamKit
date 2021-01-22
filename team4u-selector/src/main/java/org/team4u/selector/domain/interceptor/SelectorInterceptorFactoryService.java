package org.team4u.selector.domain.interceptor;

import org.team4u.base.lang.IdObjectService;

/**
 * 选择执行器工厂服务
 *
 * @author jay.wu
 */
public class SelectorInterceptorFactoryService extends IdObjectService<String, SelectorInterceptorFactory> {

    public SelectorInterceptorFactoryService() {
        super(SelectorInterceptorFactory.class);
    }
}