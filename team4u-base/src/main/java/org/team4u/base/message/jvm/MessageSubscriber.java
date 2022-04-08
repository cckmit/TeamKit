package org.team4u.base.message.jvm;

import org.team4u.base.message.MessageReceiver;

/**
 * 消息订阅者
 * <p>
 * 用于接收同一jvm内的消息
 *
 * @author jay.wu
 */
public interface MessageSubscriber<M> extends MessageReceiver<M> {

}