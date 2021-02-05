package org.team4u.ddd.infrastructure.persistence.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("stored_event")
public class StoredEventEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private long eventId;

    private String domainId;

    private String eventBody;

    private Date occurredOn;

    private String typeName;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public StoredEventEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getDomainId() {
        return domainId;
    }

    public StoredEventEntity setDomainId(String domainId) {
        this.domainId = domainId;
        return this;
    }

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
