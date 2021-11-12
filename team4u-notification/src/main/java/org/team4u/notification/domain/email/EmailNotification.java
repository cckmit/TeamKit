package org.team4u.notification.domain.email;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.team4u.notification.domain.Notification;

/**
 * 短信消息
 *
 * @author jay.wu
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class EmailNotification extends Notification {
    /**
     * 收件人邮箱地址
     */
    private String recipient;
    /**
     * 邮件标题
     */
    private String subject;
}