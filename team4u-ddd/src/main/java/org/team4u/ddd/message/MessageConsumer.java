package org.team4u.ddd.message;

import org.team4u.base.message.MessageSubscriber;

/**
 * 消息消费者
 *
 * @author jay.wu
 */
public interface MessageConsumer<M> extends MessageSubscriber<M> {

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    Class<M> messageType();
}