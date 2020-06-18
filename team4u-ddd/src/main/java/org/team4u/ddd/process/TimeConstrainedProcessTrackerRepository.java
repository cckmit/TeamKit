package org.team4u.ddd.process;

import java.util.Collection;

/**
 * 超时跟踪器资源库
 *
 * @author jay.wu
 */
public interface TimeConstrainedProcessTrackerRepository {

    /**
     * 获取所有超时的跟踪器
     *
     * @param sizeLimit 一次获取最大数量
     */
    Collection<TimeConstrainedProcessTracker> allTimedOut(int sizeLimit);

    /**
     * 获取所有超时的跟踪器
     *
     * @param processTimedOutEventType 超时事件类型
     * @param sizeLimit                一次获取最大数量
     */
    Collection<TimeConstrainedProcessTracker> allTimedOutOf(String processTimedOutEventType, int sizeLimit);

    /**
     * 获取所有跟踪器
     *
     * @param processId 关联的领域标识
     */
    Collection<TimeConstrainedProcessTracker> allTrackers(String processId);

    /**
     * 根据关联标识查找跟踪器
     *
     * @param processId                关联的领域标识
     * @param processTimedOutEventType 超时事件类型
     */
    TimeConstrainedProcessTracker trackerOfProcessId(String processId, String processTimedOutEventType);

    /**
     * 保存跟踪器
     */
    void save(TimeConstrainedProcessTracker tracker);

    /**
     * 删除跟踪器
     */
    void remove(TimeConstrainedProcessTracker tracker);
}