package org.team4u.id.domain.seq.value.cache.queue;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.log.Log;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.log.LogMessage;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceConfig;

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
        withLog(this::clear);
    }

    /**
     * 清理过期缓存
     *
     * @return 已清理数量
     */
    public int clear() {
        return queues.remove(it -> {
            if (isExpired(it.getQueue())) {
                // 关闭队列生产者
                IoUtil.close(it.getProducer());
                // 已过期，返回需要清理的key
                return it.getQueue().getContext();
            }

            return null;
        });
    }

    private void withLog(Func0<Integer> worker) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "clear");
        try {
            int count = worker.call();
            if (count > 0) {
                log.info(lm.success().append("count", count).toString());
            }
        } catch (Exception e) {
            log.error(e, lm.fail(e.getMessage()).toString());
        }
    }

    private boolean isExpired(SequenceQueue queue) {
        CacheStepSequenceConfig config = queue.getContext().getSequenceConfig();

        // 队列永不过期，直接返回
        if (!config.isQueueWillExpire()) {
            return false;
        }

        // 判断是否超出存活时间
        return System.currentTimeMillis() - queue.getStatus().getOccurredOn() > config.getExpiredWhenQueueStartedMillis();
    }

    @Override
    protected Number runIntervalMillis() {
        return 60000;
    }
}