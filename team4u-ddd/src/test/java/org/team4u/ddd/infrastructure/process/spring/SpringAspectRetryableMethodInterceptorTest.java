package org.team4u.ddd.infrastructure.process.spring;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.team4u.ddd.TestUtil;
import org.team4u.ddd.process.FakeTimeConstrainedProcessTrackerRepository;
import org.team4u.ddd.process.MockRetryStrategy;
import org.team4u.ddd.process.TimeConstrainedProcessTracker;
import org.team4u.ddd.process.retry.method.interceptor.AbstractRetryableMethodTimedOutEventSubscriber;
import org.team4u.ddd.process.retry.method.interceptor.RetryableMethodTimedOutEvent;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestRetryBeanConfig.class)
public class SpringAspectRetryableMethodInterceptorTest {

    private final Dict TEST_DICT = Dict.create().set(TestUtil.TEST_ID, TestUtil.TEST_ID);
    private final List<Integer> TEST_LIST = CollUtil.newArrayList(1, 2);

    @Autowired
    private TestRetry testRetry;
    @Autowired
    private FakeTimeConstrainedProcessTrackerRepository repository;
    @Autowired
    private AbstractRetryableMethodTimedOutEventSubscriber subscriber;

    @Before
    public void setUp() {
        testRetry.reset();
    }

    @Test
    public void invoke() {
        testRetry.handle(TestUtil.TEST_ID, TEST_DICT, TEST_LIST);

        Assert.assertEquals(TestUtil.TEST_ID, testRetry.getName());
        Assert.assertEquals(TEST_DICT, testRetry.getDict());
        Assert.assertEquals(TEST_LIST, testRetry.getAges());

        Assert.assertTrue(repository.getTrackers().isEmpty());

        TimeConstrainedProcessTracker tracker = CollUtil.getLast(repository.getRemovedTracker());
        Assert.assertNotNull(tracker);
        Assert.assertEquals(RetryableMethodTimedOutEvent.class.getName(), tracker.processTimedOutEventType());
        Assert.assertEquals(0, tracker.maxRetriesPermitted());
        Assert.assertEquals(
                TestUtil.readFile("process/springRetryableProcessTimedOutEventContext.json"),
                tracker.description()
        );
        Assert.assertNotNull(tracker.processId());
    }

    @Test
    public void onEvent() {
        TimeConstrainedProcessTracker t = new TimeConstrainedProcessTracker(
                TestUtil.TEST_ID,
                TestUtil.TEST_ID,
                TestUtil.readFile("process/springRetryableProcessTimedOutEventContext.json"),
                new Date(),
                MockRetryStrategy.INSTANCE,
                RetryableMethodTimedOutEvent.class.getName()
        );
        repository.save(t);
        subscriber.processMessage(new RetryableMethodTimedOutEvent(t.processId(), t.description()));

        ThreadUtil.sleep(1000);

        Assert.assertEquals(TestUtil.TEST_ID, testRetry.getEvent().getDomainId());
        Assert.assertEquals(TestUtil.TEST_ID, testRetry.getName());
        Assert.assertEquals(TEST_DICT, testRetry.getDict());
        Assert.assertEquals(TEST_LIST, testRetry.getAges());

        Assert.assertTrue(repository.getTrackers().isEmpty());
        TimeConstrainedProcessTracker tracker = CollUtil.getLast(repository.getRemovedTracker());
        Assert.assertEquals(TestUtil.TEST_ID, tracker.processId());
    }

    @Test
    public void onNoRetryErrorEvent() {
        TimeConstrainedProcessTracker t = new TimeConstrainedProcessTracker(
                TestUtil.TEST_ID,
                TestUtil.TEST_ID,
                TestUtil.readFile("process/errorSpringRetryableProcessTimedOutEventContext.json"),
                new Date(),
                MockRetryStrategy.INSTANCE,
                RetryableMethodTimedOutEvent.class.getName()
        );
        repository.save(t);
        subscriber.processMessage(new RetryableMethodTimedOutEvent(t.processId(), t.description()));

        ThreadUtil.sleep(1000);

        Assert.assertTrue(repository.getTrackers().isEmpty());
        TimeConstrainedProcessTracker tracker = CollUtil.getLast(repository.getRemovedTracker());
        Assert.assertEquals(TestUtil.TEST_ID, tracker.processId());
    }

}