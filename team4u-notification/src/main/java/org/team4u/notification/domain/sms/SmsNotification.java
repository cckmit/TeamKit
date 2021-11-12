package org.team4u.notification.domain.sms;

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
public class SmsNotification extends Notification {
    /**
     * 手机号码
     */
    private String mobileNum;
}