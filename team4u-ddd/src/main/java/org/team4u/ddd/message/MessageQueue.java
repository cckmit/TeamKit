package org.team4u.ddd.message;

import java.io.Closeable;

/**
 * 消息队列
 *
 * @author jay.wu
 */
public interface MessageQueue<M> extends Closeable {

    /**
     * 开始监听队列
     */
    void start();

    /**
     * 消息处理器
     *
     * @param handler 处理器
     */
    void messageHandler(MessageHandler<M> handler);
}