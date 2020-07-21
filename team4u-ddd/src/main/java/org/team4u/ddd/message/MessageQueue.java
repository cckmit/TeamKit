package org.team4u.ddd.message;

import cn.hutool.core.lang.func.VoidFunc1;

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
    void messageHandler(VoidFunc1<M> handler);

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    Class<M> messageType();
}