package org.team4u.base.log;

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
