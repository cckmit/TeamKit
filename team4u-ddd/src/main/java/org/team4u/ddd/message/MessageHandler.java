package org.team4u.ddd.message;

/**
 * 消息处理器
 * <p>
 * 负责协调messageQueue与MessageConsumer的消息流转
 *
 * @author jay.wu
 */
public interface MessageHandler<M> {

    void onMessage(M message);
}