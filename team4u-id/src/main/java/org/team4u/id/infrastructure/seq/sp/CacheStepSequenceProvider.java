package org.team4u.id.infrastructure.seq.sp;

import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.error.NestedException;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.log.LogMessage;
import org.team4u.id.domain.seq.AbstractSequenceProviderFactory;
import org.team4u.id.domain.seq.SequenceProvider;
import org.team4u.id.domain.seq.SequenceProviderFactoryHolder;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CacheStepSequenceProvider implements StepSequenceProvider {

    private final Log log = Log.get();
    private final ProviderConfig config;
    @Getter
    private final StepSequenceProvider sequenceProvider;

    private final LazyFunction<Context, QueueCounter> lazyQueueCounter = LazyFunction.of(
            LazyFunction.Config.builder().keyFunc(it -> {
                Context c = (Context) it;
                return c.id();
            }).build(),
            QueueCounter::new
    );

    public CacheStepSequenceProvider(ProviderConfig config, StepSequenceProvider sequenceProvider) {
        this.config = config;
        this.sequenceProvider = sequenceProvider;
    }

    @Override
    public Number provide(Context context) {
        return lazyQueueCounter.apply(context).next();
    }

    public int clear() {
        return lazyQueueCounter.removeIf(queueCounter -> {
            if (System.currentTimeMillis() - queueCounter.closedTime > config.getClearExpriedSec()) {
                return queueCounter.getContext();
            }

            return null;
        });
    }

    @Override
    public Config config() {
        return config;
    }

    private class QueueCounter extends LongTimeThread {
        private final Number EMPTY_NUMBER = -1;

        @Getter
        private long closedTime = 0;

        @Getter
        private final Context context;
        private BufferCounter bufferCounter;
        private final BlockingQueue<Number> cache = new LinkedBlockingQueue<>(config.getStep());

        private QueueCounter(Context context) {
            this.context = context;

            initQueue();

            start();
        }

        public Number next() {
            try {
                Number result = cache.poll(20, TimeUnit.MILLISECONDS);
                if (Objects.equals(result, EMPTY_NUMBER)) {
                    return null;
                }

                return result;
            } catch (InterruptedException e) {
                throw new NestedException(e);
            }
        }

        private void initQueue() {
            refreshBufferCounter(context);
            offer(bufferCounter);
        }

        @Override
        protected void onRun() {
            if (bufferCounter == null || bufferCounter.isEmpty() || bufferCounter.shouldRefresh()) {
                refreshBufferCounter(context);
            }

            offer(bufferCounter);
        }

        /**
         * 刷新缓冲计数器
         */
        private void refreshBufferCounter(Context context) {
            LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "refreshBufferCounter");

            Number seq = sequenceProvider.provide(context);

            if (log.isDebugEnabled()) {
                log.debug(lm.success()
                        .append("context", context)
                        .append("seq", seq)
                        .toString());
            }

            bufferCounter = new BufferCounter(seq);
        }

        private void offer(BufferCounter counter) {
            if (bufferCounter.overMaxValue()) {
                close();
                return;
            }

            do {
                Number seq = counter.next();

                try {
                    if (seq == null) {
                        cache.put(EMPTY_NUMBER);
                        close();
                        return;
                    }

                    cache.put(seq);
                } catch (InterruptedException e) {
                    close();
                    return;
                }
            } while (!counter.isEmpty());
        }

        @Override
        protected Number runIntervalMillis() {
            return 0;
        }

        @Override
        protected void onClose() {
            closedTime = System.currentTimeMillis();
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

        public boolean shouldRefresh() {
            return availableSeqPercent() < config.getMinAvailableSeqPercent();
        }
    }

    @Component
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

        private int clearExpriedSec = 60;
    }
}