package org.team4u.notification.domain;


import org.team4u.base.error.RemoteCallException;
import org.team4u.base.lang.IdObject;

/**
 * 消息发送者
 *
 * @author jay.wu
 */
public interface NotificationSender<N extends Notification> extends IdObject<String> {

    /**
     * 发送消息
     *
     * @param notification 消息体
     * @throws RemoteCallException 当底层接口调用异常时抛出
     */
    void send(N notification) throws RemoteCallException;

    enum Type {
        /**
         * 短信
         */
        SMS,
        /**
         * 应用
         */
        APP,
        /**
         * 电子邮件
         */
        EMAIL,
        /**
         * 微信
         */
        WE_CHAT
    }
}