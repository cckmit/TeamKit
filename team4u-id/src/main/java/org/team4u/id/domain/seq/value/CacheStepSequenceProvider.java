package org.team4u.id.domain.seq.value;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.error.NestedException;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.log.LogMessage;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 基于缓存的趋势增长序列服务
 *
 * @author jay.wu
 */
public class CacheStepSequenceProvider implements StepSequenceProvider {

    private final Log log = Log.get();

    private final CacheConfig config;
    @Getter
    private final StepSequenceProvider sequenceProvider;

    private final LazyFunction<Context, QueueSequenceProvider> lazyQueueCounter = LazyFunction.of(
            LazyFunction.Config.builder().keyFunc(it -> ((Context) it).id()).build(),
            QueueSequenceProvider::new
    );

    public CacheStepSequenceProvider(CacheConfig config, StepSequenceProvider sequenceProvider) {
        this.config = config;
        this.sequenceProvider = sequenceProvider;

        // 启动定时清理器
        new ClearWorker().start();
    }

    @Override
    public Number provide(Context context) {
        return lazyQueueCounter.apply(context).next();
    }

    /**
     * 清理过期缓存
     *
     * @return 已清理数量
     */
    public int clearExpiredCache() {
        return lazyQueueCounter.removeIf(it -> {
            // 已过期，返回需要清理的key
            if (it.isExpired()) {
                return it.getContext();
            }

            return null;
        });
    }

    @Override
    public CacheConfig config() {
        return config;
    }

    /**
     * 队列计数器
     */
    private class QueueSequenceProvider extends LongTimeThread {
        private final Number EMPTY_NUMBER = -1;

        private long closedTime = 0;

        @Getter
        private final Context context;
        private BufferCounter bufferCounter;
        private final BlockingQueue<Number> cache = new LinkedBlockingQueue<>(config.getStep());

        private QueueSequenceProvider(Context context) {
            this.context = context;

            if (!initQueue()) {
                return;
            }

            start();
        }

        /**
         * 从队列取出下一个序号
         */
        public Number next() {
            try {
                // 增加等待时间，避免消费速度过多，导致生产数据不足的情况
                Number result = cache.poll(config.getMaxNextTimeoutMillis(), TimeUnit.MILLISECONDS);
                if (Objects.equals(result, EMPTY_NUMBER)) {
                    return null;
                }

                return result;
            } catch (InterruptedException e) {
                throw new NestedException(e);
            }
        }

        private boolean initQueue() {
            refreshBufferCounter(context);
            return offer(bufferCounter);
        }

        @Override
        protected void onRun() {
            // 无可用序号或者低可用序号，刷新
            if (bufferCounter.isEmpty() || bufferCounter.isLowAvailableSeq()) {
                refreshBufferCounter(context);
            }

            if (!offer(bufferCounter)) {
                close();
            }
        }

        /**
         * 刷新缓冲计数器
         */
        private void refreshBufferCounter(Context context) {
            LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "refreshBufferCounter");

            Number seq = sequenceProvider.provide(context);

            if (bufferCounter == null) {
                bufferCounter = new BufferCounter(seq);
            } else {
                bufferCounter.updateMaxSeqForBuffer(seq);
            }

            if (log.isInfoEnabled()) {
                log.info(lm.success()
                        .append("context", context)
                        .append("remoteSeq", seq)
                        .append("bufferCounter", bufferCounter)
                        .toString());
            }
        }

        /**
         * 将序号放入队列
         */
        private boolean offer(BufferCounter counter) {
            if (bufferCounter.overMaxValue()) {
                return false;
            }

            do {
                Number seq = counter.next();

                try {
                    if (seq == null) {
                        cache.put(EMPTY_NUMBER);
                        return false;
                    }

                    cache.put(seq);
                } catch (InterruptedException e) {
                    return false;
                }
            } while (!counter.isEmpty());

            return true;
        }

        @Override
        protected Number runIntervalMillis() {
            return 0;
        }

        @Override
        protected void onClose() {
            closedTime = System.currentTimeMillis();
        }

        public boolean isExpired() {
            // 未关闭线程，返回未过期
            if (closedTime == 0) {
                return false;
            }

            // 已关闭线程，判断是否超出存活时间
            return System.currentTimeMillis() - closedTime > config.getExpiredWhenCloseMillis();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    /**
     * 缓冲计数器
     */
    private class BufferCounter {
        /**
         * 当前序列
         */
        private Long currentSeq;
        /**
         * 缓冲池最大序列
         */
        private Long maxSeqForBuffer;

        private BufferCounter(Number seq) {
            if (seq == null) {
                return;
            }

            this.currentSeq = seq.longValue();
            updateMaxSeqForBuffer(seq);
        }

        public void updateMaxSeqForBuffer(Number seq) {
            if (seq == null) {
                return;
            }

            if (currentSeq == null) {
                this.currentSeq = seq.longValue();
            }

            this.maxSeqForBuffer = seq.longValue() + sequenceProvider.config().getStep();
            if (maxSeqForBuffer > config.getMaxValue()) {
                maxSeqForBuffer = config.getMaxValue();
            }
        }

        /**
         * 是否已经消耗完缓冲序列
         */
        public boolean isEmpty() {
            if (currentSeq == null) {
                return true;
            }

            return currentSeq >= maxSeqForBuffer;
        }

        /**
         * 是否超过最大值
         */
        public boolean overMaxValue() {
            if (currentSeq == null) {
                return true;
            }

            return currentSeq > maxSeqForBuffer;
        }

        public Long next() {
            if (overMaxValue()) {
                return null;
            }

            Long result = currentSeq;
            currentSeq += config.getCacheStep();

            handleIfOverMaxValue();

            return result;
        }

        private void handleIfOverMaxValue() {
            if (currentSeq > maxSeqForBuffer) {
                currentSeq = null;
            }
        }

        /**
         * 剩余可用序号百分比
         */
        public int availableSeqPercent() {
            return 100 - (int) (currentSeq * 100 / maxSeqForBuffer);
        }

        /**
         * 缓冲池可用序号是否较低
         */
        public boolean isLowAvailableSeq() {
            return availableSeqPercent() < config.getMinAvailableSeqPercent();
        }

        @Override
        public String toString() {
            return currentSeq + "/" + maxSeqForBuffer;
        }
    }

    /**
     * 过期缓存清理器
     */
    private class ClearWorker extends LongTimeThread {

        @Override
        protected void onRun() {
            LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "clearExpiredCache");

            try {
                int count = clearExpiredCache();
                lm.append("count", count);
            } catch (Exception e) {
                log.info(lm.fail(e.getMessage()).toString());
            }

            log.info(lm.success().toString());
        }

        @Override
        protected Number runIntervalMillis() {
            return config.getClearExpiredRunIntervalMillis();
        }
    }

    public static class Factory extends AbstractSequenceProviderFactory<CacheConfig> {

        @Override
        public String id() {
            return "CS";
        }

        @Override
        protected SequenceProvider createWithConfig(CacheConfig config) {
            return new CacheStepSequenceProvider(config, delegateProvider(config));
        }

        @SuppressWarnings("unchecked")
        private StepSequenceProvider delegateProvider(CacheConfig config) {
            SequenceProvider.Factory<CacheConfig> factory = (SequenceProvider.Factory<CacheConfig>) holder()
                    .availablePolicyOf(config.getStepFactoryId());

            return (StepSequenceProvider) factory.create(JSONUtil.toJsonStr(config));
        }

        private SequenceProviderFactoryHolder holder() {
            return BeanProviders.getInstance().getBean(SequenceProviderFactoryHolder.class);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CacheConfig extends Config {
        /**
         * 缓存步进
         */
        private Long cacheStep = 1L;
        /**
         * 最小可用缓存百分比
         * <p>
         * 小于该百分比则异步刷新缓冲池
         */
        private int minAvailableSeqPercent = 60;
        /**
         * 真正的步进序号工厂提供者标识
         *
         * @see StepSequenceProvider.Factory
         */
        private String stepFactoryId = "DB";
        /**
         * 获取序号最大超时时间（毫秒）
         */
        private int maxNextTimeoutMillis = 20;
        /**
         * 计数器关闭后多长时间失效（毫秒）
         */
        private int expiredWhenCloseMillis = 60 * 1000;
        /**
         * 清理失效缓存间隔（毫秒）
         */
        private int clearExpiredRunIntervalMillis = 60 * 1000;
    }
}