package org.team4u.ddd.process.strategy;

import cn.hutool.core.util.RandomUtil;

/**
 * 等抖动重试间隔计算器
 * <p>
 * 在「指数间隔」和「全抖动」之间寻求平衡，降低随机性的作用
 *
 * @author jay.wu
 */
public class ExponentialRandom2RetryStrategy extends AbstractRetryStrategy {

    private Config config;

    public ExponentialRandom2RetryStrategy(Config config) {
        super(config);
        setConfig(config);
    }

    public Config getConfig() {
        return config;
    }

    private void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public int nextIntervalSec(int retryCount) {
        int base = (int) Math.pow(config.getExponentialBase(), retryCount + 1);
        return base + RandomUtil.randomInt(0, base);
    }

    public static class Config extends AbstractConfig<Config> {

        private int exponentialBase = 2;

        public int getExponentialBase() {
            return exponentialBase;
        }

        public Config setExponentialBase(int exponentialBase) {
            this.exponentialBase = exponentialBase;
            return this;
        }
    }

    public static class Factory extends AbstractJsonConfigRetryStrategyFactory<ExponentialRandom2RetryStrategy, Config> {

        @Override
        public String id() {
            return "exponentialRandom2";
        }
    }
}