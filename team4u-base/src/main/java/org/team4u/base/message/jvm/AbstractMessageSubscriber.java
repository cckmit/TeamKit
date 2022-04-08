package org.team4u.base.message.jvm;

import org.team4u.base.message.AbstractMessageReceiver;

import java.util.concurrent.ExecutorService;

/**
 * 抽象消息订阅者
 *
 * @param <M> 消息类型
 * @author jay.wu
 */
public abstract class AbstractMessageSubscriber<M> extends AbstractMessageReceiver<M> implements MessageSubscriber<M> {

    public AbstractMessageSubscriber() {
    }

    public AbstractMessageSubscriber(ExecutorService executorService) {
        super(executorService);
    }
}