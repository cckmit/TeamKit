package org.team4u.ddd.process.strategy;

/**
 * 增量重试间隔计算器
 * <p>
 * 每一次的重试间隔时间增量递增
 *
 * @author jay.wu
 */
public class IncrementRetryStrategy extends AbstractRetryStrategy {

    private Config config;

    public IncrementRetryStrategy(Config config) {
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
        return (retryCount + 1) * config.getIntervalSec();
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

    public static class Factory extends AbstractJsonConfigRetryStrategyFactory<IncrementRetryStrategy, Config> {

        @Override
        public String id() {
            return "increment";
        }
    }
}