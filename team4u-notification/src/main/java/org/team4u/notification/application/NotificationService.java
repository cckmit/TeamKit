package org.team4u.notification.application;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.error.RemoteCallException;
import org.team4u.base.registrar.PolicyRegistrar;
import org.team4u.notification.domain.Notification;
import org.team4u.notification.domain.NotificationSender;

import java.util.List;

import static org.team4u.base.log.LogService.withInfoLog;

/**
 * 消息服务
 *
 * @author jay.wu
 */
public class NotificationService extends PolicyRegistrar<String, NotificationSender<? extends Notification>> {

    private final Log log = LogFactory.get();

    public NotificationService() {
        registerPoliciesByBeanProvidersAndEvent();
    }

    public NotificationService(List<NotificationSender<? extends Notification>> objects) {
        super(objects);
    }

    /**
     * 发送消息
     *
     * @param type         消息类型
     * @param notification 消息体
     * @throws RemoteCallException 当底层接口调用异常时抛出
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void send(String type, Notification notification) throws RemoteCallException {
        withInfoLog(log, "send", (lm) -> {
            NotificationSender sender = availablePolicyOf(type);

            lm.append("notificationId", notification.getId())
                    .append("sender", sender.getClass().getSimpleName())
                    .append("type", type);

            sender.send(notification);
            return null;
        });
    }
}