package org.team4u.notification.infrastructure.app;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.riversoft.weixin.qy.base.CorpSetting;
import com.riversoft.weixin.qy.message.Messages;
import com.riversoft.weixin.qy.message.json.JsonMessage;
import com.riversoft.weixin.qy.message.json.TextMessage;
import org.team4u.base.error.RemoteCallException;
import org.team4u.base.log.LogMessage;
import org.team4u.notification.domain.app.AppNotification;
import org.team4u.notification.domain.app.AppNotificationSender;

/**
 * 基于微信企业号的推送服务
 * <p>
 * https://github.com/borball/weixin-sdk/wiki
 * https://work.weixin.qq.com/wework_admin/frame#profile
 *
 * @author jay.wu
 */
public class WechatAppNotificationSender implements AppNotificationSender {

    private final Log log = LogFactory.get();

    private final Config config;

    private final Messages messages;

    public WechatAppNotificationSender(Config config) {
        this.config = config;
        this.messages = Messages.with(
                new CorpSetting(
                        config.getAppId(),
                        config.getAppSecret()
                )
        );
    }

    @Override
    public void send(AppNotification notification) throws RemoteCallException {
        try {
            JsonMessage textMessage = new TextMessage().text(notification.getTitle() + "\n" + notification.getTemplate())
                    .safe()
                    .toUser("@all")
                    .agentId(config.getAgentId());

            messages.send(textMessage);

            log.info(LogMessage.create(this.getClass().getSimpleName(), "send")
                    .success()
                    .toString());
        } catch (Exception e) {
            throw new RemoteCallException(e);
        }
    }

    public static class Config {

        private String appId;
        private String appSecret;
        private int agentId;

        public String getAppId() {
            return appId;
        }

        public Config setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public Config setAppSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }

        public int getAgentId() {
            return agentId;
        }

        public Config setAgentId(int agentId) {
            this.agentId = agentId;
            return this;
        }
    }
}
