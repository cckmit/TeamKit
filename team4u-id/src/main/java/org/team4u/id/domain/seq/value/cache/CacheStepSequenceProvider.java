package org.team4u.id.domain.seq.value.cache;

import lombok.Getter;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.id.domain.seq.value.cache.queue.SequenceQueueContext;
import org.team4u.id.domain.seq.value.cache.queue.SequenceQueueHolder;

import java.util.concurrent.TimeUnit;

/**
 * 基于缓存的趋势增长序号服务
 * <p>
 * 主要职责：
 * <p>
 * - 同一个JVM中，多个缓存序号服务将共享相同Context标识队列
 * <p>
 * - 序号队列生产者从代理序号提供者中获取一个范围号段，计算出序号并推送到队列；当号段耗尽时，再从代理提供者重新获取号段，如此往复
 * <p>
 * - 当客户端需获取序号时，直接从队列中获取序号
 * <p>
 * - 注意：为了提升性能，一个序号类型将启用一个线程进行异步处理，需要控制线程（类型）数量，
 * 同时配置CacheStepSequenceConfig.queueExpiredMillis
 *
 * @author jay.wu
 * @see SequenceQueueHolder
 * @see CacheStepSequenceConfig
 */
public class CacheStepSequenceProvider implements SequenceProvider {

    private final CacheStepSequenceConfig config;
    @Getter
    private final SequenceQueueHolder queueHolder;
    /**
     * 代理序号提供者，负责提供号段
     */
    @Getter
    private final StepSequenceProvider delegateProvider;

    public CacheStepSequenceProvider(CacheStepSequenceConfig config,
                                     StepSequenceProvider delegateProvider,
                                     SequenceQueueHolder queueHolder) {
        this.config = config;
        this.delegateProvider = delegateProvider;
        this.queueHolder = queueHolder;
    }

    public CacheStepSequenceProvider(CacheStepSequenceConfig config,
                                     StepSequenceProvider delegateProvider) {
        this(config, delegateProvider, SequenceQueueHolder.getInstance());
    }

    @Override
    public Number provide(Context context) {
        // 增加等待时间，避免消费过快，生产不足的情况
        return queueHolder.queueOf(newQueueContext(context)).poll(
                config.getNextTimeoutMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public boolean isEmpty(Context context) {
        return queueHolder.queueOf(newQueueContext(context)).isExhausted();
    }

    private SequenceQueueContext newQueueContext(Context context) {
        return new SequenceQueueContext(context, config, delegateProvider);
    }

    public CacheStepSequenceConfig config() {
        return config;
    }
}