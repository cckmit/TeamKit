package org.team4u.id.domain.seq.value.cache;

import lombok.Getter;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.id.domain.seq.value.cache.queue.SequenceQueue;
import org.team4u.id.domain.seq.value.cache.queue.SequenceQueueCleaner;
import org.team4u.id.domain.seq.value.cache.queue.SequenceQueueProducer;

import java.util.concurrent.TimeUnit;

/**
 * 基于缓存的趋势增长序列服务
 * <p>
 * 主要职责：
 * - 负责维护不同类型的序号队列，确保不同类型的队列不会相互干扰
 * - 序号队列生产者从代理序号提供者中获取一个范围号段，计算出序号并推送到队列；当号段耗尽时，再从代理提供者重新获取号段，如此往复
 * - 当客户端需获取序号时，直接从队列中获取序号
 * - 注意：为了提升性能，一个序号类型将启用一个线程进行异步处理，需要控制线程（类型）数量
 *
 * @author jay.wu
 */
public class CacheStepSequenceProvider implements SequenceProvider {
    /**
     * 代理序号提供者，负责提供号段
     */
    @Getter
    private final StepSequenceProvider delegateProvider;

    private final CacheStepSequenceConfig config;
    @Getter
    private final SequenceQueueCleaner queueCleaner;
    private final LazyFunction<Context, SequenceQueue> queues;

    /**
     * @param config           配置
     * @param delegateProvider 代理序号提供者，负责提供真正的序号
     */
    public CacheStepSequenceProvider(CacheStepSequenceConfig config, StepSequenceProvider delegateProvider) {
        this.config = config;
        this.delegateProvider = delegateProvider;

        queues = buildQueues();
        queueCleaner = buildQueueCleaner();
    }

    private LazyFunction<Context, SequenceQueue> buildQueues() {
        return LazyFunction.of(
                LazyFunction.Config.builder().keyFunc(it -> ((Context) it).id()).build(),
                context -> {
                    SequenceQueue queue = new SequenceQueue(context, delegateProvider);
                    new SequenceQueueProducer(queue, config, delegateProvider).start();
                    return queue;
                }
        );
    }

    private SequenceQueueCleaner buildQueueCleaner() {
        if (!config.shouldStartClearWorker()) {
            return null;
        }

        SequenceQueueCleaner queueCleaner = new SequenceQueueCleaner(config, queues);
        queueCleaner.start();

        return queueCleaner;
    }

    @Override
    public Number provide(Context context) {
        // 增加等待时间，避免消费速度过快，生产不足的情况
        return queues.apply(context).poll(config.getNextTimeoutMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isEmpty(Context context) {
        return queues.apply(context).isExhausted();
    }

    public CacheStepSequenceConfig config() {
        return config;
    }
}