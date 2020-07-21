package org.team4u.ddd.message;

/**
 * 消息生产者
 *
 * @author jay.wu
 */
public interface MessageProducer {

    /**
     * 发送消息
     *
     * @param message 消息体
     */
    void send(Object message);
}