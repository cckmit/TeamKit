package org.team4u.base.message;

/**
 * 消息订阅者
 *
 * @author jay.wu
 */
public interface MessageSubscriber<M> {

    /**
     * 处理消息
     *
     * @param message 消息体
     */
    void processMessage(M message);

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    Class<M> messageType();
}