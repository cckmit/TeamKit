package org.team4u.ddd.message;

/**
 * 消息消费者
 *
 * @author jay.wu
 */
public interface MessageConsumer<M> {

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