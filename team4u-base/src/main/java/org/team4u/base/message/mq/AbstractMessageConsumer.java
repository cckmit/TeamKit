package org.team4u.base.message.mq;

import org.team4u.base.message.AbstractMessageReceiver;

import java.util.concurrent.ExecutorService;

/**
 * 抽象消息消费者
 *
 * @param <M> 消息类型
 * @author jay.wu
 */
public abstract class AbstractMessageConsumer<M> extends AbstractMessageReceiver<M> implements MessageConsumer<M> {

    public AbstractMessageConsumer() {
    }

    public AbstractMessageConsumer(ExecutorService executorService) {
        super(executorService);
    }
}