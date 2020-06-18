package org.team4u.ddd.infrastructure.persistence.mybatis;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("stored_event")
public class StoredEventEntity {

    private String eventBody;

    private long eventId;

    private Date occurredOn;

    private String typeName;

    private Date createTime;

    private Date updateTime;

    public String getEventBody() {
        return eventBody;
    }

    public StoredEventEntity setEventBody(String eventBody) {
        this.eventBody = eventBody;
        return this;
    }

    public long getEventId() {
        return eventId;
    }

    public StoredEventEntity setEventId(long eventId) {
        this.eventId = eventId;
        return this;
    }

    public Date getOccurredOn() {
        return occurredOn;
    }

    public StoredEventEntity setOccurredOn(Date occurredOn) {
        this.occurredOn = occurredOn;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public StoredEventEntity setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public StoredEventEntity setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public StoredEventEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
