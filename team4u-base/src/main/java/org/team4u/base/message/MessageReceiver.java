package org.team4u.base.message;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ClassUtil;

/**
 * 消息接收器
 *
 * @author jay.wu
 */
public interface MessageReceiver<M> {

    /**
     * 处理消息
     *
     * @param message 消息体
     */
    void onMessage(M message);

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @SuppressWarnings("unchecked")
    default Class<M> messageType() {
        return Singleton.get(getClass().getName(), () -> (Class<M>) ClassUtil.getTypeArgument(this.getClass()));
    }

    /**
     * 判断是否监听指定的事件类型
     */
    default boolean supports(Object message) {
        return messageType().isAssignableFrom(message.getClass());
    }
}