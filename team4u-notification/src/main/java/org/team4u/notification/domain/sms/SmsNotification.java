package org.team4u.notification.domain.sms;

import cn.hutool.core.lang.Dict;
import org.team4u.notification.domain.Notification;

/**
 * 短信消息
 *
 * @author jay.wu
 */
public class SmsNotification extends Notification {
    /**
     * 手机号码
     */
    private String mobileNum;

    public String getMobileNum() {
        return mobileNum;
    }


    public static final class Builder {
        protected String id;
        protected String template;
        protected String templateId;
        protected Dict templateVars;
        protected Dict extend;
        private String mobileNum;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withTemplate(String template) {
            this.template = template;
            return this;
        }

        public Builder withTemplateId(String templateId) {
            this.templateId = templateId;
            return this;
        }

        public Builder withTemplateVars(Dict templateVars) {
            this.templateVars = templateVars;
            return this;
        }

        public Builder withExtend(Dict extend) {
            this.extend = extend;
            return this;
        }

        public SmsNotification build() {
            SmsNotification smsNotification = new SmsNotification();
            smsNotification.templateVars = this.templateVars;
            smsNotification.mobileNum = this.mobileNum;
            smsNotification.id = this.id;
            smsNotification.template = this.template;
            smsNotification.extend = this.extend;
            smsNotification.templateId = this.templateId;
            return smsNotification;
        }
    }
}