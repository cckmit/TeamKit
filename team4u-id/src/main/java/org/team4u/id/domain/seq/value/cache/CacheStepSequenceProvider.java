package org.team4u.id.domain.seq.value.cache;

import lombok.Getter;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.id.domain.seq.value.cache.queue.SequenceQueueContext;
import org.team4u.id.domain.seq.value.cache.queue.SequenceQueueHolder;

import java.util.concurrent.TimeUnit;

/**
 * 基于缓存的趋势增长序列服务
 * <p>
 * 主要职责：
 * <p>
 * - 负责维护不同类型的序号队列，确保不同类型的队列不会相互干扰
 * <p>
 * - 序号队列生产者从代理序号提供者中获取一个范围号段，计算出序号并推送到队列；当号段耗尽时，再从代理提供者重新获取号段，如此往复
 * <p>
 * - 当客户端需获取序号时，直接从队列中获取序号
 * <p>
 * - 注意：为了提升性能，一个序号类型将启用一个线程进行异步处理，需要控制线程（类型）数量，
 * 同时配置CacheStepSequenceConfig.expiredWhenQueueStartedMillis
 *
 * @author jay.wu
 * @see CacheStepSequenceConfig
 */
public class CacheStepSequenceProvider implements SequenceProvider {

    private final CacheStepSequenceConfig config;
    /**
     * 代理序号提供者，负责提供号段
     */
    @Getter
    private final StepSequenceProvider delegateProvider;
    @Getter
    private final SequenceQueueHolder sequenceQueueHolder;

    public CacheStepSequenceProvider(CacheStepSequenceConfig config,
                                     StepSequenceProvider delegateProvider,
                                     SequenceQueueHolder sequenceQueueHolder) {
        this.config = config;
        this.delegateProvider = delegateProvider;
        this.sequenceQueueHolder = sequenceQueueHolder;
    }

    public CacheStepSequenceProvider(CacheStepSequenceConfig config,
                                     StepSequenceProvider delegateProvider) {
        this(config, delegateProvider, SequenceQueueHolder.getInstance());
    }

    @Override
    public Number provide(Context context) {
        // 增加等待时间，避免消费过快，生产不足的情况
        return sequenceQueueHolder.queueOf(newQueueContext(context)).poll(
                config.getNextTimeoutMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public boolean isEmpty(Context context) {
        return sequenceQueueHolder.queueOf(newQueueContext(context)).isExhausted();
    }

    private SequenceQueueContext newQueueContext(Context context) {
        return new SequenceQueueContext(context, config, delegateProvider);
    }

    public CacheStepSequenceConfig config() {
        return config;
    }
}