package org.team4u.kit.core.log;

/**
 * 日志消息渲染器
 *
 * @author jay.wu
 */
public interface LogMessageRender {

    /**
     * 渲染日志消息
     */
    String render(LogMessage logMessage);
}
