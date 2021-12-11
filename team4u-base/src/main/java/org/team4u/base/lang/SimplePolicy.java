package org.team4u.base.lang;


/**
 * 简单策略服务
 *
 * @author jay.wu
 * @see org.team4u.base.registrar.Policy
 * @deprecated 已废弃，使用Policy代替
 */
public interface SimplePolicy<C> {

    /**
     * 是否支持该上下文
     *
     * @param context 　上下文
     */
    boolean isSupport(C context);

    /**
     * 执行策略
     *
     * @param context 上下文
     */
    void execute(C context);
}