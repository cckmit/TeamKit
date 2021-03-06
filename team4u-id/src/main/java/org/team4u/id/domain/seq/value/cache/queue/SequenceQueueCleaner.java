package org.team4u.id.domain.seq.value.cache.queue;

import org.team4u.base.lang.LongTimeThread;

/**
 * 序号队列清理器
 * <p>
 * - 职责：清理内存中的过期序号队列，以及关闭队列生产者线程
 * - 原因：缓存中的序号队列会不断增加，若不及时清理会导致内存溢出（如按照 小时 分组，每个小时将生成一条记录）或者线程耗尽
 */
public class SequenceQueueCleaner extends LongTimeThread {

    private final SequenceQueueHolder queueHolder;

    public SequenceQueueCleaner(SequenceQueueHolder queueHolder) {
        this.queueHolder = queueHolder;
    }

    @Override
    protected void onRun() {
        clear();
    }

    /**
     * 清理过期队列集合
     *
     * @return 已清理数量
     */
    public int clear() {
        return (int) queueHolder.values()
                .stream()
                .filter(this::clear)
                .count();
    }

    /**
     * 清理过期队列
     */
    private boolean clear(SequenceQueueHolder.Value value) {
        if (!value.getQueue().isExpired()) {
            return false;
        }

        queueHolder.remove(value.getQueue().getContext());
        return true;
    }

    @Override
    protected boolean isSleepBeforeRun() {
        return true;
    }

    @Override
    protected Number runIntervalMillis() {
        return 5000;
    }
}