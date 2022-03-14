package org.team4u.id.domain.seq.value.cache.queue;

import cn.hutool.log.Log;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.log.LogMessage;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceConfig;

/**
 * 序号队列清理器
 * <p>
 * - 职责：清理内存中的过期序号队列
 * - 原因：缓存中的序号队列会不断增加，若不及时清理会导致内存溢出（如按照 小时 分组，每个小时将生成一条记录）
 */
public class SequenceQueueCleaner extends LongTimeThread {

    private final Log log = Log.get();

    private final CacheStepSequenceConfig config;

    private final LazyFunction<SequenceProvider.Context, SequenceQueue> queues;

    public SequenceQueueCleaner(CacheStepSequenceConfig config,
                                LazyFunction<SequenceProvider.Context, SequenceQueue> queues) {
        this.queues = queues;
        this.config = config;
    }

    @Override
    protected void onRun() {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "clear");

        try {
            int count = clear();
            lm.append("count", count);
        } catch (Exception e) {
            log.info(lm.fail(e.getMessage()).toString());
        }

        log.info(lm.success().toString());
    }

    /**
     * 清理过期缓存
     *
     * @return 已清理数量
     */
    public int clear() {
        return queues.remove(it -> {
            // 已过期，返回需要清理的key
            if (isExpired(it)) {
                return it.getContext();
            }

            return null;
        });
    }

    private boolean isExpired(SequenceQueue queue) {
        // 队列未耗尽或者永不过期，直接返回
        if (!queue.isExhausted() || !config.shouldExpiredWhenQueueExhausted()) {
            return false;
        }

        // 判断是否超出存活时间
        return System.currentTimeMillis() - queue.getStatus().getOccurredOn() > config.getExpiredWhenQueueExhaustedMillis();
    }


    @Override
    protected Number runIntervalMillis() {
        return config.getQueueCleanerRunIntervalMillis();
    }
}