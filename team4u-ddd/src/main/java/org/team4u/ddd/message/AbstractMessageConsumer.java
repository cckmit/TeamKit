package org.team4u.ddd.message;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.base.log.LogService;

import java.util.concurrent.ExecutorService;

/**
 * 抽象多事件处理器
 * <p>
 * 事件总线在接收到一个领域事件时，先调用supports(event)方法判断当前监听器是否支持该事件，
 * 如果支持则调用onEvent()方法处理该事件
 *
 * @param <V>
 */
public abstract class AbstractMessageConsumer<V> implements MessageConsumer {

    protected final Log log = LogFactory.get();

    /**
     * 队列堆积任务超过阈值未处理，丢弃处理
     */
    private final ExecutorService executorService;


    protected AbstractMessageConsumer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void onMessage(Object message) {
        if (!supports(message)) {
            return;
        }

        if (executorService == null) {
            logAndHandle(message);
            return;
        }

        executorService.execute(() -> logAndHandle(message));
    }

    private void logAndHandle(V message) {
        LogMessage lm = LogMessages.createWithMasker(this.getClass().getSimpleName(), "onMessage")
                .append("message", message);

        try {
            handle(message);
            log.info(lm.success().toString());
        } catch (Throwable e) {
            LogService.logForError(log, lm, e);
        }
    }


    /**
     * 处理消息
     *
     * @param message 要处理的事件
     */
    protected abstract void handle(V message) throws Throwable;

    /**
     * 判断是否监听指定的事件类型
     */
    protected boolean supports(Object message) {
        return messageType().isAssignableFrom(message.getClass());
    }


    @SuppressWarnings("unchecked")
    protected Class<V> messageType() {
        return (Class<V>) ClassUtil.getTypeArgument(this.getClass());
    }
}