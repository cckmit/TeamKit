package org.team4u.notification.infrastructure.kz;

import cn.hutool.core.util.IdUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.notification.domain.SendFailureException;
import org.team4u.notification.domain.app.AppNotification;

public class KuaizhiAppNotificationSenderTest {

    @Test
    public void send() {
        KuaizhiAppNotificationSender sender = new KuaizhiAppNotificationSender(
                new KuaizhiAppNotificationSender.Config()
                        .setJobId("test")
                        .setUrl("https://api.kzfeed.com/bot/$token/pushMessage")
        );

        try {
            sender.send(AppNotification.Builder
                    .create()
                    .withTitle(IdUtil.fastSimpleUUID())
                    .withTemplate(IdUtil.fastSimpleUUID())
                    .build());
            Assert.fail();
        } catch (SendFailureException e) {
            Assert.assertEquals(
                    e.getMessage(),
                    "{\"errno\":10002,\"errmsg\":\"\\u673a\\u5668\\u4ebatoken\\u9519\\u8bef\"}"
            );
        }
    }
}