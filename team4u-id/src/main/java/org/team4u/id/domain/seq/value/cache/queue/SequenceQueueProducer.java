package org.team4u.id.domain.seq.value.cache.queue;

import cn.hutool.log.Log;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.log.LogMessage;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceConfig;

/**
 * 序号队列生产者
 *
 * @author jay.wu
 */
public class SequenceQueueProducer extends LongTimeThread {
    private final Log log = Log.get();

    private final SequenceQueue queue;
    /**
     * 号段
     */
    private final SequenceSegment segment;

    private final StepSequenceProvider delegateProvider;

    public SequenceQueueProducer(SequenceQueue queue,
                                 CacheStepSequenceConfig sequenceConfig,
                                 StepSequenceProvider delegateProvider) {
        this.queue = queue;
        this.segment = new SequenceSegment(sequenceConfig, delegateProvider.config());
        this.delegateProvider = delegateProvider;

        // 创建时即进行预热，避免线程未完全启动，导致客户端无法获取序号的情况
        onRun();
    }

    @Override
    protected void onRun() {
        // 当前号段已耗尽，尝试获取新号段
        if (segment.isEmpty()) {
            refreshSegment();
        }

        if (!offerAllCurrentSegmentSequences()) {
            //无可用序号，关闭线程
            close();
        }
    }

    /**
     * 更新号段
     * <p>
     * 所有操作均在同一线程调用，无需考虑并发问题
     */
    private void refreshSegment() {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "refreshSegment");

        // 从代理序号提供者获取下一个批次的号段
        Number seq = delegateProvider.provide(queue.getContext());
        // 更新号段
        segment.refreshSegment(seq);

        if (log.isInfoEnabled()) {
            log.info(lm.success()
                    .append("context", queue.getContext())
                    .append("delegateSeq", seq)
                    .append("segment", segment)
                    .toString());
        }
    }

    /**
     * 将当前号段所有序号放入队列
     *
     * @return 是否有号段入队列
     */
    private boolean offerAllCurrentSegmentSequences() {
        do {
            Number seq = segment.next();

            try {
                // 若队列中缓存序号已满，将阻塞线程
                queue.put(seq);

                if (seq == null) {
                    return false;
                }
            } catch (InterruptedException e) {
                log.error(e, LogMessage.create(this.getClass().getSimpleName(), "offerAllCurrentSegmentSequences")
                        .fail(e.getMessage())
                        .append("segment", segment)
                        .toString()
                );
                return false;
            }
        } while (!segment.isEmpty());

        return true;
    }

    @Override
    protected Number runIntervalMillis() {
        // 通过队列的put进行阻塞，故无需设置循环间隔
        return 0;
    }
}