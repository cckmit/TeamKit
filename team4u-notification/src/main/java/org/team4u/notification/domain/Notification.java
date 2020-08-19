package org.team4u.notification.domain;

import cn.hutool.core.lang.Dict;

/**
 * 消息体
 *
 * @author jay.wu
 */
public class Notification {

    /**
     * 消息标识，建议用UUID保持唯一性
     */
    protected String id;
    /**
     * 模板内容
     */
    protected String template;
    /**
     * 模板标识
     */
    protected String templateId;
    /**
     * 模板参数
     */
    protected Dict templateVars;
    /**
     * 扩展信息
     */
    protected Dict extend;

    public String getId() {
        return id;
    }

    public String getTemplate() {
        return template;
    }

    public String getTemplateId() {
        return templateId;
    }

    public Dict getTemplateVars() {
        return templateVars;
    }

    public Dict getExtend() {
        return extend;
    }
}
