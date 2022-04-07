package org.team4u.base.message.mq;

import org.team4u.base.message.MessageSubscriber;

/**
 * 消息消费者
 *
 * @author jay.wu
 */
public interface MessageConsumer<M> extends MessageSubscriber<M> {
}