package org.team4u.ddd.infrastructure.persistence.mybatis;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import java.util.Date;

@TableName("published_notification_tracker")
public class PublishedNotificationTrackerEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String trackerId;

    private Long mostRecentPublishedNotificationId;

    private String typeName;

    @Version
    private Long version;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public PublishedNotificationTrackerEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTrackerId() {
        return trackerId;
    }

    public PublishedNotificationTrackerEntity setTrackerId(String trackerId) {
        this.trackerId = trackerId;
        return this;
    }

    public Long getMostRecentPublishedNotificationId() {
        return mostRecentPublishedNotificationId;
    }

    public PublishedNotificationTrackerEntity setMostRecentPublishedNotificationId(Long mostRecentPublishedNotificationId) {
        this.mostRecentPublishedNotificationId = mostRecentPublishedNotificationId;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public PublishedNotificationTrackerEntity setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public PublishedNotificationTrackerEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public PublishedNotificationTrackerEntity setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public PublishedNotificationTrackerEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}