package org.team4u.ddd.domain.model;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.base.log.LogService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 基于本地线程池的事件订阅者
 *
 * @author jay.wu
 */
public abstract class ThreadPoolEventSubscriber<E extends DomainEvent> extends AbstractSingleDomainEventSubscriber<E> {

    protected final Log log = LogFactory.get();

    /**
     * 队列堆积任务超过阈值未处理，丢弃处理
     */
    private ExecutorService executorService;

    @Override
    protected void handle(DomainEvent event) throws Throwable {
        if (executorService() == null) {
            super.handle(event);
        } else {
            executorService().execute(() -> {
                LogMessage lm = LogMessages.createWithMasker(this.getClass().getSimpleName(), "handle")
                        .append("event", event);

                try {
                    super.handle(event);

                    log.info(lm.success().toString());
                } catch (Throwable e) {
                    LogService.logForError(log, lm, e);
                }
            });
        }
    }

    /**
     * 自定义ExecutorService
     */
    protected ExecutorService executorService() {
        if (executorService == null) {
            executorService = ExecutorBuilder
                    .create()
                    .setCorePoolSize(3)
                    .setWorkQueue(new LinkedBlockingQueue<>(2000))
                    .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                    .build();
        }

        return executorService;
    }
}