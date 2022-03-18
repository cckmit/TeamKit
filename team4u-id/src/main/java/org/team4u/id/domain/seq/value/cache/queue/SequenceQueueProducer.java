package org.team4u.id.domain.seq.value.cache.queue;

import cn.hutool.log.Log;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.log.LogMessage;

import java.util.concurrent.TimeUnit;

/**
 * 序号队列生产者
 * <p>
 * - 启动一个线程，异步刷新号段，并将当前号段内所有可用序号推送到队列
 * - 所有操作在均同一个线程完成，无需考虑并发问题
 *
 * @author jay.wu
 */
public class SequenceQueueProducer extends LongTimeThread {

    private final Log log = Log.get();

    private final SequenceQueue queue;
    private final SequenceSegment segment;
    private final SequenceQueueContext context;

    public SequenceQueueProducer(SequenceQueue queue, SequenceQueueContext context) {
        this.queue = queue;
        this.context = context;
        this.segment = new SequenceSegment(
                context.getSequenceConfig(),
                context.getDelegateProvider().config()
        );

        setName(queue.getContext().id());
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
        Number seq = context.getDelegateProvider().provide(context.getProviderContext());
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
                // 若队列中缓存序号已满，将阻塞，除非线程被关闭
                putAndWait(seq);

                // 无可用序号，退出
                if (seq == null) {
                    return false;
                }
            } catch (InterruptedException e) {
                log.info(LogMessage.create(this.getClass().getSimpleName(), "offerAllCurrentSegmentSequences")
                        .fail(e.getMessage())
                        .append("segment", segment)
                        .toString()
                );
                return false;
            }
        } while (!segment.isEmpty());

        return true;
    }

    private void putAndWait(Number seq) throws InterruptedException {
        // 阻塞直到推送成功
        while (!queue.offer(seq, context.getSequenceConfig().getQueueOfferTimeoutMillis(), TimeUnit.MILLISECONDS)) {
            // 线程已被关闭，退出
            if (isClosed()) {
                throw new InterruptedException("Thread is closed");
            }
        }
    }

    @Override
    protected Number runIntervalMillis() {
        // 通过队列的offer进行阻塞，故无需设置循环间隔
        return 0;
    }
}