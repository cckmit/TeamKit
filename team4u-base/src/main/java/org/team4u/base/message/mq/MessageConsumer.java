package org.team4u.base.message.mq;

import org.team4u.base.message.MessageReceiver;

/**
 * 消息消费者
 * <p>
 * 用于接收MQ内的消息
 *
 * @author jay.wu
 */
public interface MessageConsumer<M> extends MessageReceiver<M> {
}