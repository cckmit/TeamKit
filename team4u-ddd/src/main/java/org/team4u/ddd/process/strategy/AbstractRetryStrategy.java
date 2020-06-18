package org.team4u.ddd.process.strategy;

/**
 * 抽象重试策略
 *
 * @author jay.wu
 */
public abstract class AbstractRetryStrategy implements RetryStrategy {

    private final AbstractConfig<?> config;

    protected AbstractRetryStrategy(AbstractConfig<?> config) {
        this.config = config;
    }

    @Override
    public int maxRetriesPermitted() {
        return config.getMaxRetriesPermitted();
    }

    @Override
    public String strategyId() {
        return config.getId();
    }

    /**
     * 抽象重试策略配置
     *
     * @param <T> 配置实现类类型
     */
    public abstract static class AbstractConfig<T> {
        /**
         * 重试策略标识
         */
        private String id;
        /**
         * 允许最大重试次数
         */
        private int maxRetriesPermitted = Integer.MAX_VALUE;

        public String getId() {
            return id;
        }

        public T setId(String id) {
            this.id = id;
            //noinspection unchecked
            return (T) this;
        }

        public int getMaxRetriesPermitted() {
            return maxRetriesPermitted;
        }

        public T setMaxRetriesPermitted(int maxRetriesPermitted) {
            this.maxRetriesPermitted = maxRetriesPermitted;
            //noinspection unchecked
            return (T) this;
        }
    }
}