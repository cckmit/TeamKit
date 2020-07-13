package org.team4u.selector.domain.interceptor.entity;


import org.team4u.base.lang.IdObject;

/**
 * 选择拦截器构建工厂
 *
 * @author jay.wu
 */
public interface SelectorInterceptorFactory extends IdObject<String> {

    /**
     * 根据配置创建选择拦截器
     *
     * @param config bean或者map
     */
    SelectorInterceptor create(Object config);
}
