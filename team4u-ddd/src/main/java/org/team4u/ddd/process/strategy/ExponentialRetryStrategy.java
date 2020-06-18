package org.team4u.ddd.process.strategy;

/**
 * 指数重试间隔计算器
 * <p>
 * 每一次的重试间隔呈指数级增加
 *
 * @author jay.wu
 */
public class ExponentialRetryStrategy extends AbstractRetryStrategy {

    private Config config;

    public ExponentialRetryStrategy(Config config) {
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
        return (int) Math.pow(config.getExponentialBase(), retryCount + 1);
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

    public static class Factory extends AbstractJsonConfigRetryStrategyFactory<ExponentialRetryStrategy, Config> {

        @Override
        public String id() {
            return "exponential";
        }
    }
}