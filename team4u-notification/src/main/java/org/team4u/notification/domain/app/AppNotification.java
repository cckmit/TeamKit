package org.team4u.notification.domain.app;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.team4u.notification.domain.Notification;

/**
 * 应用消息
 *
 * @author jay.wu
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AppNotification extends Notification {
    /**
     * 消息类型
     */
    @Builder.Default
    private Type type = Type.UNICAST;

    /**
     * 标题
     */
    private String title;

    /**
     * 子标题
     */
    private String subTitle;

    /**
     * 消息图片地址
     */
    private String imgUrl;

    /**
     * 重定向调整地址
     */
    private String redirectUrl;


    public enum Type {
        /**
         * 单播，即指定用户
         */
        UNICAST,
        /**
         * 广播，即所有用户
         */
        BROADCAST
    }
}