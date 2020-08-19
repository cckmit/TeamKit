package org.team4u.notification.domain.email;

import cn.hutool.core.lang.Dict;
import org.team4u.notification.domain.Notification;

/**
 * 短信消息
 *
 * @author jay.wu
 */
public class EmailNotification extends Notification {
    /**
     * 收件人邮箱地址
     */
    private String recipient;
    /**
     * 邮件标题
     */
    private String subject;

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }


    public static final class Builder {
        protected String id;
        protected String template;
        protected String templateId;
        protected Dict templateVars;
        protected Dict extend;
        private String recipient;
        private String subject;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withRecipient(String recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder withSubject(String subject) {
            this.subject = subject;
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

        public EmailNotification build() {
            EmailNotification emailNotification = new EmailNotification();
            emailNotification.templateVars = this.templateVars;
            emailNotification.id = this.id;
            emailNotification.template = this.template;
            emailNotification.recipient = this.recipient;
            emailNotification.extend = this.extend;
            emailNotification.subject = this.subject;
            emailNotification.templateId = this.templateId;
            return emailNotification;
        }
    }
}