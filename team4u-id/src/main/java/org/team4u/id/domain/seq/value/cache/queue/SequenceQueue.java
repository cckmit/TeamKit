package org.team4u.id.domain.seq.value.cache.queue;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import org.team4u.base.error.NestedException;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceConfig;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 序号队列
 * <p>
 * - 客户端获取序号时，直接从队列中poll即可，能获得较好的性能
 * - 序号由SequenceQueueProducer负责推送
 *
 * @see SequenceQueueProducer
 */
@Getter
public class SequenceQueue {
    /**
     * 无可用序列时返回此值
     * <p>
     * 队列无法设置null值，使用其他值代替该含义
     */
    private static final Number NULL_SEQ = -1;
    /**
     * 队列状态
     */
    private Status status = Status.CREATED;
    /**
     * 队列创建时间戳
     */
    private final long createdAt = System.currentTimeMillis();
    /**
     * 上下文信息
     */
    private final SequenceQueueContext context;
    /**
     * 缓存队列
     * <p>
     * 队列大小为代理序号提供者步长，即号段长度
     */
    private final BlockingQueue<Number> cache;

    public SequenceQueue(SequenceQueueContext context) {
        this.context = context;
        this.cache = buildCache(context.getCacheConfig().maxCacheSeqSize());
    }

    public boolean offer(Number seq, long timeout, TimeUnit unit) throws InterruptedException {
        return cache.offer(ObjectUtil.defaultIfNull(seq, NULL_SEQ), timeout, unit);
    }

    /**
     * 从队列取出下一个序号
     */
    public Number poll(long timeout, TimeUnit unit) {
        // 已无可用序号，直接跳过，避免无效等待时间
        if (isExhausted()) {
            return null;
        }

        try {
            Number seq = cache.poll(timeout, unit);

            // 无序号可用，直接返回null
            if (isNull(seq)) {
                exhaust();
                return null;
            }

            return seq;
        } catch (InterruptedException e) {
            throw new NestedException(e);
        }
    }

    public boolean isExhausted() {
        return status == Status.EXHAUSTED;
    }

    public boolean isChange(CacheStepSequenceConfig newCacheConfig) {
        return newCacheConfig.maxCacheSeqSize() != context.getCacheConfig().getMaxCacheSeqSize();
    }

    private LinkedBlockingQueue<Number> buildCache(int maxCacheSeqSize) {
        return new LinkedBlockingQueue<>(maxCacheSeqSize);
    }

    private boolean isNull(Number result) {
        return Objects.equals(result, NULL_SEQ);
    }

    private void exhaust() {
        status = Status.EXHAUSTED;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "|" + context.id();
    }

    /**
     * 序号队列状态
     *
     * @author jay.wu
     */
    public enum Status {
        CREATED,
        EXHAUSTED
    }
}