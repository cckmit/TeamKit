package org.team4u.notification.infrastructure.app;

import org.junit.Test;
import org.team4u.notification.domain.app.AppNotification;

public class TelegramAppNotificationSenderTest {

    @Test
    public void send() {
        TelegramAppNotificationSender sender = new TelegramAppNotificationSender(
                TelegramAppNotificationSender.Config.builder()
                        .token("x")
                        .chatId("x")
                        .build());

        sender.send(AppNotification.builder()
                .title("测试")
                .template("1")
                .build());
    }
}