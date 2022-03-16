package org.team4u.base.message;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.error.NestedException;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogService;

import java.util.concurrent.ExecutorService;

/**
 * 抽象消息订阅者
 *
 * @param <M> 消息类型
 * @author jay.wu
 */
public abstract class AbstractMessageSubscriber<M> implements MessageSubscriber<M> {

    protected final Log log = LogFactory.get(this.getClass());

    /**
     * 线程池
     */
    private final ExecutorService executorService;


    protected AbstractMessageSubscriber() {
        this(null);
    }

    protected AbstractMessageSubscriber(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void onMessage(M message) {
        if (!supports(message)) {
            return;
        }

        if (executorService == null) {
            onMessageWithLog(message);
            return;
        }

        executorService.execute(() -> {
            try {
                onMessageWithLog(message);
            } catch (Exception e) {
                // ignore error
            }
        });
    }

    protected void onMessageWithLog(M message) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "onMessage")
                .append("messageType", message.getClass().getSimpleName())
                .append("message", message);

        try {
            internalOnMessage(message);
            log.info(lm.success().toString());
        } catch (Throwable e) {
            LogService.logForError(log, lm, e);
            throw NestedException.wrap(e);
        }
    }

    /**
     * 内部处理消息
     *
     * @param message 要处理的事件
     * @throws Throwable 异常
     */
    protected abstract void internalOnMessage(M message) throws Throwable;
}