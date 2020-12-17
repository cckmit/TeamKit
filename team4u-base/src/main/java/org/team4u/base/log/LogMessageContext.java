package org.team4u.base.log;

/**
 * 日志消息上下文
 *
 * @author jay.wu
 */
public class LogMessageContext {

    private static final ThreadLocal<LogMessage> CONTEXT = new ThreadLocal<>();

    /**
     * 获取日志消息
     */
    public static LogMessage get() {
        return CONTEXT.get();
    }

    /**
     * 设置日志消息
     */
    public static LogMessage set(LogMessage lm) {
        CONTEXT.set(lm);
        return lm;
    }

    /**
     * 删除日志消息
     */
    public static void remove() {
        CONTEXT.remove();
    }

    /**
     * 创建并设置日志消息
     */
    public static LogMessage createAndSet(String module, String eventName) {
        return set(LogMessage.create(module, eventName));
    }

    /**
     * 从当前线程中获取消息日志，若不存在则创建后返回
     */
    public static LogMessage getOrCreate(String module, String eventName) {
        LogMessage lm = get();

        if (lm != null) {
            return lm;
        }

        return LogMessage.create(module, eventName);
    }
}