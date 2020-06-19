package org.team4u.selector.domain.interceptor.service;

import org.team4u.selector.domain.interceptor.entity.SelectorInterceptorFactory;
import org.team4u.core.lang.IdObjectService;

import java.util.ServiceLoader;

/**
 * 选择执行器工厂服务
 *
 * @author jay.wu
 */
public class SelectorInterceptorFactoryService extends IdObjectService<String, SelectorInterceptorFactory> {

    public SelectorInterceptorFactoryService() {
        ServiceLoader<SelectorInterceptorFactory> serviceLoader = ServiceLoader.load(SelectorInterceptorFactory.class);

        for (SelectorInterceptorFactory interceptor : serviceLoader) {
            saveIdObject(interceptor);
        }
    }
}