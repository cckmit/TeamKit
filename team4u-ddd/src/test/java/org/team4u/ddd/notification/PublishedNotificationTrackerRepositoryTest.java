package org.team4u.ddd.notification;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.ddd.TestUtil;
import org.team4u.ddd.infrastructure.persistence.mybatis.MybatisPublishedNotificationTrackerRepository;
import org.team4u.ddd.infrastructure.persistence.mybatis.PublishedNotificationTrackerMapper;
import org.team4u.test.spring.SpringDbTest;
import org.team4u.test.spring.DbTestBeanConfig;

@ComponentScan("org.team4u.ddd.infrastructure.persistence.mybatis")
@ContextConfiguration(classes = DbTestBeanConfig.class)
public class PublishedNotificationTrackerRepositoryTest extends SpringDbTest {
    @Autowired
    private PublishedNotificationTrackerMapper mapper;

    @Test
    public void publishedNotificationTracker() {
        PublishedNotificationTracker tracker = repository().publishedNotificationTracker();
        Assert.assertNotNull(tracker);
        Assert.assertEquals(0, tracker.mostRecentPublishedNotificationId());
    }

    @Test
    public void trackMostRecentPublishedNotification() {
        PublishedNotificationTracker tracker = new PublishedNotificationTracker(TestUtil.TEST_ID);
        repository().trackMostRecentPublishedNotification(
                tracker,
                CollUtil.newArrayList(
                        new Notification(1L, new FakeDomainEvent(TestUtil.TEST_ID)),
                        new Notification(2L, new FakeDomainEvent(TestUtil.TEST_ID))
                )
        );

        Assert.assertEquals(2L, tracker.mostRecentPublishedNotificationId());

        tracker = repository().publishedNotificationTracker();
        Assert.assertNotNull(tracker);
        Assert.assertEquals(2L, tracker.mostRecentPublishedNotificationId());
    }

    @Test
    public void typeName() {
        PublishedNotificationTracker tracker = repository().publishedNotificationTracker();
        Assert.assertEquals(TestUtil.TEST_ID, tracker.typeName());
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{
                "sql/notification_tracker.sql"
        };
    }

    private PublishedNotificationTrackerRepository repository() {
        return new MybatisPublishedNotificationTrackerRepository(TestUtil.TEST_ID, mapper);
    }
}