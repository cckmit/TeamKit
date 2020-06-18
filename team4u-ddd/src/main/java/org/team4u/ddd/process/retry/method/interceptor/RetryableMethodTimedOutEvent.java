package org.team4u.ddd.process.retry.method.interceptor;

import org.team4u.ddd.process.ProcessTimedOutEvent;

/**
 * 可重试方法超时事件
 *
 * @author jay.wu
 */
public class RetryableMethodTimedOutEvent extends ProcessTimedOutEvent {

    private static final ThreadLocal<RetryableMethodTimedOutEvent> CURRENT_EVENT = new ThreadLocal<>();

    public RetryableMethodTimedOutEvent(String processId, String description) {
        super(processId, description);
    }

    public RetryableMethodTimedOutEvent(String processId, int totalRetriesPermitted, int retryCount, String description) {
        super(processId, totalRetriesPermitted, retryCount, description);
    }

    static void setCurrentEvent(RetryableMethodTimedOutEvent event) {
        CURRENT_EVENT.set(event);
    }

    static void removeCurrentEvent() {
        CURRENT_EVENT.remove();
    }

    /**
     * 获取当前方法关联的超时事件
     *
     * @return 超时事件
     */
    public static RetryableMethodTimedOutEvent currentEvent() {
        return CURRENT_EVENT.get();
    }

    /**
     * 当前方法是否存在关联的超时时间
     *
     * @return true:存在，false：不存在
     */
    public static boolean hasCurrentEvent() {
        return currentEvent() != null;
    }
}