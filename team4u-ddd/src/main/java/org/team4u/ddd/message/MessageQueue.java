package org.team4u.ddd.message;

/**
 * 消息生产者
 *
 * @author jay.wu
 */
public interface MessageQueue {

    /**
     * 开始监听队列
     */
    void start();

    /**
     * 接收消息
     *
     * @param message 消息体
     */
    void onMessage(Object message);
}