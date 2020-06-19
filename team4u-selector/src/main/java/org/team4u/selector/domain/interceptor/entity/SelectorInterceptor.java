package org.team4u.selector.domain.interceptor.entity;


import org.team4u.selector.domain.selector.entity.binding.SelectorBinding;

/**
 * 选择器拦截器
 *
 * @author jay.wu
 */
public interface SelectorInterceptor {

    /**
     * 前置处理
     * <p>
     * 选择执行前
     *
     * @return 返回的绑定参数将覆盖原有值
     */
    SelectorBinding preHandle(SelectorBinding binding);

    /**
     * 后置处理
     * <p>
     * 选择执行后
     *
     * @param value 选择原始选择结果
     * @return 返回值将覆盖原有结果
     */
    String postHandle(SelectorBinding binding, String value);
}