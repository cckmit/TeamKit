package org.team4u.ddd.notification;


import cn.hutool.core.lang.Assert;
import org.team4u.ddd.domain.model.ConcurrencySafeAggregateRoot;

/**
 * 已发布通知跟踪器
 *
 * @author jay.wu
 */
public class PublishedNotificationTracker extends ConcurrencySafeAggregateRoot {

    private String publishedNotificationTrackerId;

    private String typeName;

    private long mostRecentPublishedNotificationId;

    public PublishedNotificationTracker(String aTypeName) {
        this();

        this.setTypeName(aTypeName);
    }

    protected PublishedNotificationTracker() {
        super();
    }

    public long mostRecentPublishedNotificationId() {
        return this.mostRecentPublishedNotificationId;
    }

    public void setMostRecentPublishedNotificationId(long aMostRecentPublishedNotificationId) {
        this.mostRecentPublishedNotificationId = aMostRecentPublishedNotificationId;
    }

    public String publishedNotificationTrackerId() {
        return this.publishedNotificationTrackerId;
    }

    public String typeName() {
        return this.typeName;
    }

    @Override
    public String toString() {
        return "PublishedNotificationTracker [mostRecentPublishedNotificationId=" + mostRecentPublishedNotificationId
                + ", publishedNotificationTrackerId=" + publishedNotificationTrackerId + ", typeName=" + typeName + "]";
    }

    public void setPublishedNotificationTrackerId(String publishedNotificationTrackerId) {
        this.publishedNotificationTrackerId = publishedNotificationTrackerId;
    }

    protected void setTypeName(String aTypeName) {
        Assert.notEmpty(aTypeName, "The tracker type name is required.");
        this.typeName = aTypeName;
    }
}