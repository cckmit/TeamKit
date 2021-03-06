package org.team4u.base.message;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.concurrent.ExecutorService;

import static org.team4u.base.log.LogService.withInfoLog;

/**
 * 抽象消息接收者
 *
 * @param <M> 消息类型
 * @author jay.wu
 */
public abstract class AbstractMessageReceiver<M> implements MessageReceiver<M> {

    protected final Log log = LogFactory.get(this.getClass());

    /**
     * 线程池
     */
    private final ExecutorService executorService;


    protected AbstractMessageReceiver() {
        this(null);
    }

    protected AbstractMessageReceiver(ExecutorService executorService) {
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
            onMessageWithLog(message);
        });
    }

    protected void onMessageWithLog(M message) {
        withInfoLog(log, "onMessage", (lm) -> {
            lm.append("messageType", message.getClass().getSimpleName())
                    .append("message", message);
            internalOnMessage(message);
            return null;
        });
    }

    /**
     * 内部处理消息
     *
     * @param message 要处理的事件
     */
    protected abstract void internalOnMessage(M message) throws Exception;
}