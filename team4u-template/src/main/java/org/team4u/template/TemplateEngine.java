package org.team4u.template;

import java.util.Map;

/**
 * 模板引擎
 * <p>
 * 用于动态渲染模板
 *
 * @author jay.wu
 */
public interface TemplateEngine {

    /**
     * 渲染模板
     *
     * @param template 模板内容
     * @param bindings 模板变量
     */
    String render(String template, Map<String, Object> bindings);
}