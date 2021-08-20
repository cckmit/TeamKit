package org.team4u.selector.domain.interceptor;


import org.team4u.base.registrar.StringIdPolicy;

/**
 * 选择拦截器构建工厂
 *
 * @author jay.wu
 */
public interface SelectorInterceptorFactory extends StringIdPolicy {

    /**
     * 根据配置创建选择拦截器
     *
     * @param config bean或者map
     */
    SelectorInterceptor create(Object config);
}
