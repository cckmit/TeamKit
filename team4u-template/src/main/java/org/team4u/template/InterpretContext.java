package org.team4u.template;

import org.team4u.core.lang.EasyMap;

/**
 * 模板交互上下文
 *
 * @author jay.wu
 */
public class InterpretContext {

    private static ThreadLocal<InterpretContext> contextThreadLocal = new ThreadLocal<>();

    /**
     * 变量栈
     */
    private EasyMap valueStack = new EasyMap();

    /**
     * 已渲染内容(未最终完成渲染时将动态变化)
     */
    private String renderedContent;

    /**
     * 获取当前模板交互上下文
     */
    public static InterpretContext current() {
        InterpretContext context = contextThreadLocal.get();

        if (context != null) {
            return context;
        }

        context = new InterpretContext();
        contextThreadLocal.set(context);
        return context;
    }

    /**
     * 当前模板变量集合
     */
    public EasyMap valueStack() {
        return valueStack;
    }

    /**
     * 获取已渲染内容
     * <p>
     * 注意：未最终完成渲染时，该值将动态变化
     */
    public String renderedContent() {
        return renderedContent;
    }

    /**
     * 设置已渲染内容
     */
    public InterpretContext setRenderedContent(String renderedContent) {
        this.renderedContent = renderedContent;
        return this;
    }
}