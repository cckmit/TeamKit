package org.team4u.ddd.domain.model;

import org.team4u.base.message.Event;

import java.util.Date;

/**
 * 领域事件接口
 *
 * @author jay.wu
 */
public interface DomainEvent extends Event {

    /**
     * 获取领域标识
     */
    String getDomainId();

    /**
     * 获得事件发生时间
     *
     * @return 事件发生时间
     */
    Date getOccurredOn();

    /**
     * 获得版本
     *
     * @return 事件的版本
     */
    int getVersion();
}