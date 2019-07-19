package org.team4u.kit.core.event.api;


import cn.hutool.core.util.IdUtil;

import java.util.Date;

/**
 * 领域事件基类，领域事件代表具有业务含义的事件
 *
 * @author jay.wu
 */
public abstract class AbstractEvent implements Event {

    private String id = IdUtil.simpleUUID();

    private Date occurredOn;

    private int version;

    public AbstractEvent() {
        this(new Date(), 1);
    }

    /**
     * @param occurredOn 发生时间
     */
    public AbstractEvent(Date occurredOn) {
        this(occurredOn, 1);
    }

    /**
     * @param occurredOn 发生时间
     * @param version    版本
     */
    public AbstractEvent(Date occurredOn, int version) {
        if (occurredOn == null) {
            throw new IllegalArgumentException("occurredOn is null");
        }

        this.occurredOn = new Date(occurredOn.getTime());
        this.version = version;
    }

    /**
     * 获得事件ID
     *
     * @return 事件的ID
     */
    @Override
    public String id() {
        return id;
    }

    /**
     * 获得事件发生时间
     *
     * @return 事件发生时间
     */
    @Override
    public Date occurredOn() {
        return occurredOn;
    }

    /**
     * 获得版本
     *
     * @return 事件的版本
     */
    @Override
    public int version() {
        return version;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AbstractEvent)) {
            return false;
        }
        AbstractEvent that = (AbstractEvent) other;
        return this.id().equals(that.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}