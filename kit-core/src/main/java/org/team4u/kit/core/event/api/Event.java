package org.team4u.kit.core.event.api;

import java.util.Date;

/**
 * 事件接口
 *
 * @author jay.wu
 */
public interface Event {

    /**
     * 获得事件ID
     *
     * @return 事件的ID
     */
    String id();

    /**
     * 获得事件发生时间
     *
     * @return 事件发生时间
     */
    Date occurredOn();

    /**
     * 获得版本
     *
     * @return 事件的版本
     */
    int version();
}
