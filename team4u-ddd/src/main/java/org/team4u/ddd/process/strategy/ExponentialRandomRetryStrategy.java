package org.team4u.ddd.process.strategy;

import cn.hutool.core.util.RandomUtil;

/**
 * 全抖动重试间隔计算器
 * <p>
 * 在递增的基础上，增加随机性（可以把其中的指数增长部分替换成增量增长）
 * <p>
 * 适用于将某一时刻集中产生的大量重试请求进行压力分散的场景。
 *
 * @author jay.wu
 */
public class ExponentialRandomRetryStrategy extends AbstractRetryStrategy {

    private Config config;

    public ExponentialRandomRetryStrategy(Config config) {
        super(config);
        setConfig(config);
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public int nextIntervalSec(int retryCount) {
        int base = (int) Math.pow(config.getExponentialBase(), retryCount + 1);
        return RandomUtil.randomInt(config.getMinIntervalSec(), base);
    }

    public static class Config extends AbstractConfig<Config> {

        private int minIntervalSec = 0;
        private int exponentialBase = 2;

        public int getMinIntervalSec() {
            return minIntervalSec;
        }

        public Config setMinIntervalSec(int minIntervalSec) {
            this.minIntervalSec = minIntervalSec;
            return this;
        }

        public int getExponentialBase() {
            return exponentialBase;
        }

        public Config setExponentialBase(int exponentialBase) {
            this.exponentialBase = exponentialBase;
            return this;
        }
    }

    public static class Factory extends AbstractJsonConfigRetryStrategyFactory<ExponentialRandomRetryStrategy, Config> {

        @Override
        public String id() {
            return "exponentialRandom";
        }
    }
}