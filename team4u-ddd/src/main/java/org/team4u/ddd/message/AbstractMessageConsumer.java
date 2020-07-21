package org.team4u.ddd.message;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.error.NestedException;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.base.log.LogService;

import java.util.concurrent.ExecutorService;

/**
 * 抽象消息消费者
 *
 * @param <M> 消息类型
 * @author jay.wu
 */
public abstract class AbstractMessageConsumer<M> implements MessageConsumer<M> {

    protected final Log log = LogFactory.get();

    /**
     * 队列堆积任务超过阈值未处理，丢弃处理
     */
    private final ExecutorService executorService;


    protected AbstractMessageConsumer() {
        this(null);
    }

    protected AbstractMessageConsumer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void processMessage(M message) {
        if (!supports(message)) {
            return;
        }

        if (executorService == null) {
            logAndProcessMessage(message);
            return;
        }

        executorService.execute(() -> {
            try {
                logAndProcessMessage(message);
            } catch (Exception e) {
                // ignore error
            }
        });
    }

    protected void logAndProcessMessage(M message) {
        LogMessage lm = LogMessages.createWithMasker(this.getClass().getSimpleName(), "processMessage")
                .append("messageType", message.getClass().getSimpleName())
                .append("message", message);

        try {
            internalProcessMessage(message);
            log.info(lm.success().toString());
        } catch (Throwable e) {
            LogService.logForError(log, lm, e);

            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new NestedException(e);
            }
        }
    }


    /**
     * 内部处理消息
     *
     * @param message 要处理的事件
     * @throws Throwable 异常
     */
    protected abstract void internalProcessMessage(M message) throws Throwable;

    /**
     * 判断是否监听指定的事件类型
     */
    protected boolean supports(M message) {
        return messageType().isAssignableFrom(message.getClass());
    }


    @Override
    @SuppressWarnings("unchecked")
    public Class<M> messageType() {
        return (Class<M>) ClassUtil.getTypeArgument(this.getClass());
    }
}