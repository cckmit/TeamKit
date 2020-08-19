package org.team4u.notification.infrastructure.jike;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.error.RemoteCallException;
import org.team4u.base.log.LogMessage;
import org.team4u.notification.domain.SendFailureException;
import org.team4u.notification.domain.app.AppNotification;
import org.team4u.notification.domain.app.AppNotificationSender;

/**
 * 快知推送
 *
 * @author jay.wu
 */
public class KuaizhiAppNotificationSender implements AppNotificationSender {

    private final Log log = LogFactory.get();

    private final Config config;

    public KuaizhiAppNotificationSender(Config config) {
        this.config = config;
    }

    @Override
    public void send(AppNotification notification) throws RemoteCallException {
        try {
            String result = HttpUtil.post(
                    config.getUrl(), "{\n" +
                            "  \"job_id\": \"" + config.getJobId() + "\",\n" +
                            "  \"cards\": [\n" +
                            "    {\n" +
                            "      \"title\": \"" + notification.getTitle() + "\",\n" +
                            "      \"text\": \"" + notification.getTemplate() + "\"\n" +
                            "    }\n" +
                            "  ] " +
                            "}\n", config.getTimeoutSec()
            );

            checkResult(result);

            log.info(LogMessage.create(this.getClass().getSimpleName(), "send")
                    .success()
                    .append("result", result)
                    .toString());
        } catch (HttpException e) {
            throw new RemoteCallException(e);
        }
    }

    private void checkResult(String result) {
        String errorCode = JSONUtil.parse(result).getByPath("errno", String.class);
        if (!StrUtil.equals(errorCode, "0")) {
            throw new SendFailureException(result);
        }
    }

    public static class Config {

        private String url;

        private String jobId;

        private int timeoutSec = 3000;

        public String getUrl() {
            return url;
        }

        public Config setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getJobId() {
            return jobId;
        }

        public Config setJobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public int getTimeoutSec() {
            return timeoutSec;
        }

        public Config setTimeoutSec(int timeoutSec) {
            this.timeoutSec = timeoutSec;
            return this;
        }
    }
}
