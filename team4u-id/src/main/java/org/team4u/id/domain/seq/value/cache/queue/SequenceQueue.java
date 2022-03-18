package org.team4u.id.domain.seq.value.cache.queue;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import org.team4u.base.error.NestedException;

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
     * 队列创建时间戳
     */
    private final long createdAt;
    /**
     * 队列状态
     */
    private SequenceQueueStatus status;
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
        this.cache = new LinkedBlockingQueue<>(context.getDelegateProvider().config().getStep());
        this.status = SequenceQueueStatus.CREATED;
        this.createdAt = System.currentTimeMillis();

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
        return status == SequenceQueueStatus.EXHAUSTED;
    }

    private boolean isNull(Number result) {
        return Objects.equals(result, NULL_SEQ);
    }

    private void exhaust() {
        status = SequenceQueueStatus.EXHAUSTED;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}