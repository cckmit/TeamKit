package org.team4u.base.registrar.factory;

/**
 * 策略工厂
 *
 * @param <C> 配置类型
 * @param <P> 策略类型
 * @author jay.wu
 */
public interface PolicyFactory<C, P> {
    /**
     * 根据配置创建策略
     *
     * @param configId 配置标识
     * @param config   配置
     * @return 创建的对象
     */
    P create(String configId, C config);
}