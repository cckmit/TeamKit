package org.team4u.id.domain.seq.value.cache.queue;

import cn.hutool.core.io.IoUtil;
import cn.hutool.log.Log;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.log.LogMessageContext;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceConfig;

import static org.team4u.base.log.LogService.withInfoLog;

/**
 * 序号队列清理器
 * <p>
 * - 职责：清理内存中的过期序号队列，以及关闭队列生产者线程
 * - 原因：缓存中的序号队列会不断增加，若不及时清理会导致内存溢出（如按照 小时 分组，每个小时将生成一条记录）或者线程耗尽
 */
public class SequenceQueueCleaner extends LongTimeThread {

    private final Log log = Log.get();

    private final LazyFunction<SequenceQueueContext, SequenceQueueHolder.HolderValue> queues;

    public SequenceQueueCleaner(LazyFunction<SequenceQueueContext, SequenceQueueHolder.HolderValue> queues) {
        this.queues = queues;
    }

    @Override
    protected void onRun() {
        clear();
    }

    /**
     * 清理过期缓存
     *
     * @return 已清理数量
     */
    public int clear() {
        return withInfoLog(log, "clear", () -> {
            int count = queues.remove(this::close);
            LogMessageContext.get().append("count", count);
            return count;
        });
    }

    /**
     * 关闭队列生产者
     *
     * @return 返回关闭队列的上下文
     */
    private SequenceQueueContext close(SequenceQueueHolder.HolderValue holderValue) {
        if (!isExpired(holderValue.getQueue())) {
            return null;
        }

        // 关闭队列生产者
        IoUtil.close(holderValue.getProducer());
        return holderValue.getQueue().getContext();
    }

    private boolean isExpired(SequenceQueue queue) {
        CacheStepSequenceConfig config = queue.getContext().getSequenceConfig();

        // 队列永不过期，直接返回
        if (!config.isQueueWillExpire()) {
            return false;
        }

        // 判断是否超出存活时间
        return System.currentTimeMillis() - queue.getCreatedAt() > config.getQueueExpiredMillis();
    }

    @Override
    protected boolean isSleepBeforeRun() {
        return true;
    }

    @Override
    protected Number runIntervalMillis() {
        return 60000;
    }
}