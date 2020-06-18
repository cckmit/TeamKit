package org.team4u.ddd.infrastructure.persistence.mybatis;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.team4u.ddd.notification.Notification;
import org.team4u.ddd.notification.PublishedNotificationTracker;
import org.team4u.ddd.notification.PublishedNotificationTrackerRepository;

import java.util.Date;
import java.util.List;

public class MybatisPublishedNotificationTrackerRepository implements PublishedNotificationTrackerRepository {

    private String typeName;

    private PublishedNotificationTrackerMapper mapper;

    public MybatisPublishedNotificationTrackerRepository(String typeName,
                                                         PublishedNotificationTrackerMapper mapper) {
        this.typeName = typeName;
        this.mapper = mapper;
    }

    @Override
    public PublishedNotificationTracker publishedNotificationTracker() {
        return publishedNotificationTracker(typeName());
    }

    @Override
    public PublishedNotificationTracker publishedNotificationTracker(String typeName) {
        PublishedNotificationTrackerEntity entity = mapper.selectOne(
                new LambdaQueryWrapper<PublishedNotificationTrackerEntity>()
                        .eq(PublishedNotificationTrackerEntity::getTypeName, typeName)
        );

        if (entity == null) {
            return new PublishedNotificationTracker(typeName);
        }

        PublishedNotificationTracker tracker = new PublishedNotificationTracker(entity.getTypeName());
        tracker.setMostRecentPublishedNotificationId(entity.getMostRecentPublishedNotificationId());
        tracker.setConcurrencyVersion(entity.getVersion());
        tracker.setId(entity.getId());
        tracker.setPublishedNotificationTrackerId(entity.getTrackerId());

        return tracker;
    }

    @Override
    public void trackMostRecentPublishedNotification(PublishedNotificationTracker tracker,
                                                     List<Notification> notifications) {
        Notification notification = CollUtil.getLast(notifications);

        if (notification == null) {
            return;
        }

        tracker.setMostRecentPublishedNotificationId(notification.getNotificationId());

        PublishedNotificationTrackerEntity entity = new PublishedNotificationTrackerEntity()
                .setId(tracker.getId())
                .setVersion(tracker.concurrencyVersion())
                .setMostRecentPublishedNotificationId(tracker.mostRecentPublishedNotificationId())
                .setTrackerId(tracker.publishedNotificationTrackerId())
                .setTypeName(tracker.typeName());


        if (entity.getId() == null) {
            entity.setCreateTime(new Date());
            mapper.insert(entity);
        } else {
            if (mapper.updateById(entity) == 0) {
                tracker.failWhenConcurrencyViolation();
            }
        }
    }

    @Override
    public String typeName() {
        return typeName;
    }
}