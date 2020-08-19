package org.team4u.notification.domain.app;

import cn.hutool.core.lang.Dict;
import org.team4u.notification.domain.Notification;

/**
 * 应用消息
 *
 * @author jay.wu
 */
public class AppNotification extends Notification {
    /**
     * 消息类型
     */
    private Type type = Type.UNICAST;

    /**
     * 标题
     */
    private String title;

    /**
     * 子标题
     */
    private String subTitle;

    /**
     * 消息图片地址
     */
    private String imgUrl;

    /**
     * 重定向调整地址
     */
    private String redirectUrl;


    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public enum Type {
        /**
         * 单播，即指定用户
         */
        UNICAST,
        /**
         * 广播，即所有用户
         */
        BROADCAST
    }


    public static final class Builder {
        protected String id;
        protected String template;
        protected String templateId;
        protected Dict templateVars;
        protected Dict extend;
        private Type type = Type.UNICAST;
        private String title;
        private String subTitle;
        private String imgUrl;
        private String redirectUrl;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withSubTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public Builder withImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Builder withRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
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

        public AppNotification build() {
            AppNotification appNotification = new AppNotification();
            appNotification.title = this.title;
            appNotification.type = this.type;
            appNotification.templateVars = this.templateVars;
            appNotification.id = this.id;
            appNotification.template = this.template;
            appNotification.subTitle = this.subTitle;
            appNotification.extend = this.extend;
            appNotification.imgUrl = this.imgUrl;
            appNotification.templateId = this.templateId;
            appNotification.redirectUrl = this.redirectUrl;
            return appNotification;
        }
    }
}