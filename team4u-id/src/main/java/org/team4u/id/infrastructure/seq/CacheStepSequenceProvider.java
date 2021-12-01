package org.team4u.id.infrastructure.seq;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.error.NestedException;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.id.domain.seq.AbstractSequenceProviderFactory;
import org.team4u.id.domain.seq.SequenceProvider;
import org.team4u.id.domain.seq.SequenceProviderFactoryHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

public class CacheStepSequenceProvider implements StepSequenceProvider {

    private final ProviderConfig config;
    @Getter
    private final StepSequenceProvider sequenceProvider;

    private final ExecutorService taskExecutor = ThreadUtil.newExecutor(5);
    private final LazyFunction<String, DoubleBufferCounter> lazyCounter = LazyFunction.of(s -> new DoubleBufferCounter());

    public CacheStepSequenceProvider(ProviderConfig config, StepSequenceProvider sequenceProvider) {
        this.config = config;
        this.sequenceProvider = sequenceProvider;
    }

    @Override
    public Number provide(Context context) {
        // 缓冲计数器key
        String bufferCounterKey = context.getSequenceConfig().getTypeId() + ":" + context.getGroupKey();
        // 加载有效的缓冲计数器
        BufferCounter bufferCounter = lazyCounter.apply(bufferCounterKey).loadAvailableCounter(context);
        // 获取当前计数并递增
        return bufferCounter.next();
    }

    @Override
    public Config config() {
        return config;
    }

    /**
     * 双缓冲计数器
     */
    private class DoubleBufferCounter {
        /**
         * 当前使用缓冲器的索引
         */
        private int currentIndex = 0;
        /**
         * 刷新缓冲器的异步任务
         */
        private FutureTask<BufferCounter> refreshBufferCounterTask;
        /**
         * 内部缓冲计数器数组
         */
        private final BufferCounter[] bufferCounters = new BufferCounter[2];

        /**
         * 获取当前正在使用的缓冲计数器
         */
        private BufferCounter getCurrentCounter() {
            return bufferCounters[currentIndex];
        }

        /**
         * 加载有效的缓冲计数器，若已用完，将刷新后返回
         */
        public synchronized BufferCounter loadAvailableCounter(Context context) {
            BufferCounter counter = getCurrentCounter();

            if (counter != null) {
                // 已超过最大值
                if (counter.overMaxValue()) {
                    return counter;
                }

                // 缓冲计数器未用完
                if (!counter.isEmpty()) {
                    // 可用缓冲计数小于70%，且无可用刷新缓冲计数器
                    if (counter.availableSeqPercent() < config.getMinAvailableSeqPercent() && refreshBufferCounterTask == null) {
                        asyncRefreshBufferCounter(context);
                    }

                    return counter;
                }
            }

            // 缓冲计数器已用完且无可用刷新缓冲计数器任务
            if (refreshBufferCounterTask == null) {
                asyncRefreshBufferCounter(context);
            }

            try {
                // 等待结果
                counter = refreshBufferCounterTask.get();
                // 更新当前索引位置
                currentIndex = nextIndex();
                bufferCounters[currentIndex] = counter;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new NestedException(e);
            } finally {
                // 清空刷新缓冲计数器任务
                refreshBufferCounterTask = null;
            }

            return counter;
        }

        /**
         * 异步刷新缓冲计数器
         */
        private void asyncRefreshBufferCounter(Context context) {
            refreshBufferCounterTask = new FutureTask<>(() -> refreshBufferCounter(context));
            taskExecutor.execute(refreshBufferCounterTask);
        }

        /**
         * 刷新缓冲计数器
         */
        private BufferCounter refreshBufferCounter(Context context) {
            Number seq = sequenceProvider.provide(context);
            return new BufferCounter(seq);
        }

        /**
         * 下一个索引
         */
        private int nextIndex() {
            return (currentIndex + 1) % bufferCounters.length;
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

        private BufferCounter(Number currentSeq) {
            if (currentSeq != null) {
                this.currentSeq = currentSeq.longValue();
                this.maxSeqForBuffer = this.currentSeq + sequenceProvider.config().getStep();
                if (maxSeqForBuffer > config.getMaxValue()) {
                    maxSeqForBuffer = config.getMaxValue();
                }
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
            if (maxSeqForBuffer == null) {
                return true;
            }
            return currentSeq > config.getMaxValue();
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
            if (currentSeq > config.getMaxValue()) {
                currentSeq = config.getMaxValue();
            }
        }

        /**
         * 剩余可用序号百分比
         */
        public int availableSeqPercent() {
            return 100 - (int) (currentSeq * 100 / maxSeqForBuffer);
        }
    }

    public static class Factory extends AbstractSequenceProviderFactory {

        @Override
        public String id() {
            return "CACHE";
        }

        @Override
        protected SequenceProvider internalCreate(JSONObject jsonConfig) {
            ProviderConfig config = jsonConfig.toBean(ProviderConfig.class);
            SequenceProviderFactoryHolder holder = BeanProviders.getInstance().getBean(
                    SequenceProviderFactoryHolder.class
            );
            return new CacheStepSequenceProvider(
                    config,
                    (StepSequenceProvider) holder.availablePolicyOf(config.getProviderId()).create(jsonConfig)
            );
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ProviderConfig extends Config {
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
         * 真正的序号提供者标识
         */
        private String providerId = "DB";
    }
}