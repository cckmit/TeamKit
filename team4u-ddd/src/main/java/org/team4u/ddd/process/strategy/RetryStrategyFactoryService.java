package org.team4u.ddd.process.strategy;


import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 重试策略工厂服务
 *
 * @author jay.wu
 */
public class RetryStrategyFactoryService extends PolicyRegistrar<String, RetryStrategyFactory<?, ?>> {

    /**
     * 创建重试策略
     *
     * @param factoryId 策略工厂标识
     * @param config    策略配置内容
     * @return 重试策略
     */
    public RetryStrategy create(String factoryId, String config) {
        StringConfigRetryStrategyFactory<?, ?> factory = (StringConfigRetryStrategyFactory<?, ?>) policyOf(factoryId);

        if (factory == null) {
            return null;
        }

        return factory.create(config);
    }
}