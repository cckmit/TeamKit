package org.team4u.notification.domain;

import cn.hutool.core.lang.Dict;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 消息体
 *
 * @author jay.wu
 */
@Data
@SuperBuilder
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
}
