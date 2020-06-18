package org.team4u.ddd.process.strategy;

/**
 * 固定重试间隔计算器
 * <p>
 * 每一次的重试间隔时间固定
 *
 * @author jay.wu
 */
public class FixedRetryStrategy extends AbstractRetryStrategy {

    private Config config;

    public FixedRetryStrategy(Config config) {
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
        return config.getIntervalSec();
    }

    public static class Config extends AbstractConfig<Config> {

        private int intervalSec;

        public int getIntervalSec() {
            return intervalSec;
        }

        public Config setIntervalSec(int intervalSec) {
            this.intervalSec = intervalSec;
            return this;
        }
    }

    public static class Factory extends AbstractJsonConfigRetryStrategyFactory<FixedRetryStrategy, Config> {

        @Override
        public String id() {
            return "fixed";
        }
    }
}