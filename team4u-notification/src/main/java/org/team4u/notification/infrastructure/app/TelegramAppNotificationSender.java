package org.team4u.notification.infrastructure.app;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team4u.base.error.RemoteCallException;
import org.team4u.base.log.LogMessage;
import org.team4u.notification.domain.app.AppNotification;
import org.team4u.notification.domain.app.AppNotificationSender;

/**
 * 基于Telegram的推送服务
 * <p>
 * https://github.com/pengrad/java-telegram-bot-api#send-message
 * https://zsxwz.com/2021/05/07/%e4%bd%bf%e7%94%a8%e7%94%b5%e6%8a%a5telegram-bot%e5%ae%9e%e7%8e%b0%e6%b6%88%e6%81%af%e6%8e%a8%e9%80%81/
 *
 * @author jay.wu
 */
public class TelegramAppNotificationSender implements AppNotificationSender {

    private final Log log = LogFactory.get();

    private final Config config;

    private final TelegramBot bot;

    public TelegramAppNotificationSender(Config config) {
        this.config = config;
        this.bot = new TelegramBot(config.getToken());

    }

    @Override
    public void send(AppNotification notification) throws RemoteCallException {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "send")
                .append("title", notification.toString());

        try {
            SendMessage request = new SendMessage(
                    config.getChatId(),
                    String.format("<b>%is</b>\n%s", notification.getTitle(), notification.getTemplate())
            ).parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(false);

            SendResponse sendResponse = bot.execute(request);
            if (sendResponse.isOk()) {
                log.info(lm.success().toString());
            } else {
                log.warn(lm.fail(sendResponse.toString()).toString());
            }
        } catch (Exception e) {
            throw new RemoteCallException(e);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {

        private String chatId;
        private String token;
    }
}
