package org.team4u.ddd.infrastructure.persistence.mybatis;

import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.ddd.TestUtil;
import org.team4u.ddd.infrastructure.persistence.memory.LogOnlyEventStore;
import org.team4u.ddd.process.ProcessTimedOutEvent;
import org.team4u.ddd.process.TimeConstrainedProcessTracker;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerRepository;
import org.team4u.ddd.process.strategy.FakeRetryStrategyRepository;
import org.team4u.ddd.process.strategy.FixedRetryStrategy;
import org.team4u.test.DbTest;
import org.team4u.test.TestBeanConfig;

import javax.annotation.PostConstruct;

@ContextConfiguration(classes = TestBeanConfig.class)
public class MybatisTimeConstrainedProcessTrackerRepositoryTest extends DbTest {

    @Autowired
    private TimeConstrainedProcessTrackerMapper mapper;

    private TimeConstrainedProcessTrackerRepository repository;

    @PostConstruct
    public void beforeClass() {
        repository = new MybatisTimeConstrainedProcessTrackerRepository(
                new LogOnlyEventStore(),
                mapper,
                new FakeRetryStrategyRepository());
    }

    @Test
    public void allTimedOut() {
        repository.save(tracker(1));
        Assert.assertEquals(1, repository.allTimedOut(2).size());
    }

    @Test
    public void allNotTimedOut() {
        repository.save(tracker(Integer.MAX_VALUE));
        Assert.assertTrue(repository.allTimedOut(2).isEmpty());
    }

    @Test
    public void allTimedOutOf() {
        repository.save(tracker(1));

        Assert.assertEquals(1, repository.allTimedOutOf(ProcessTimedOutEvent.class.getName(), 2).size());
        Assert.assertTrue(repository.allTimedOutOf(TimeConstrainedProcessTracker.class.getName(), 2).isEmpty());
    }

    @Test
    public void allNotTimedOutOf() {
        repository.save(tracker(Integer.MAX_VALUE));
        Assert.assertTrue(repository.allTimedOutOf(ProcessTimedOutEvent.class.getName(), 2).isEmpty());
    }

    @Test
    public void allTrackers() {
        repository.save(tracker(1));

        Assert.assertEquals(1, repository.allTrackers(TestUtil.TEST_ID).size());
        Assert.assertTrue(repository.allTrackers("1").isEmpty());
    }

    @Test
    public void trackerOfProcessId() {
        repository.save(tracker(1));
        TimeConstrainedProcessTracker tracker = repository.trackerOfProcessId(TestUtil.TEST_ID,
                ProcessTimedOutEvent.class.getName());
        Assert.assertNotNull(tracker);

        tracker = repository.trackerOfProcessId("1", ProcessTimedOutEvent.class.getName());
        Assert.assertNull(tracker);
    }

    @Test
    public void save() {
        repository.save(tracker(1));
        TimeConstrainedProcessTracker tracker = repository.trackerOfProcessId(TestUtil.TEST_ID,
                ProcessTimedOutEvent.class.getName());
        Assert.assertEquals(TestUtil.TEST_ID, tracker.trackerId());
        Assert.assertEquals(TestUtil.TEST_ID, tracker.processId());
        Assert.assertEquals(TestUtil.TEST_ID, tracker.description());
        Assert.assertEquals(ProcessTimedOutEvent.class.getName(), tracker.processTimedOutEventType());
        Assert.assertEquals(1, tracker.retryCount());
        Assert.assertNotNull(tracker.retryStrategy());
        Assert.assertEquals(1, tracker.retryIntervalSec());
        Assert.assertEquals(DateUtil.parse("2020-04-07 00:00:02"), tracker.timeoutOccursOn());
        Assert.assertFalse(tracker.isProcessInformedOfTimeout());
        Assert.assertTrue(tracker.hasTimedOut());
        Assert.assertFalse(tracker.isCompleted());
        Assert.assertEquals(0, tracker.concurrencyVersion());
    }

    @Test
    public void remove() {
        repository.save(tracker(1));
        TimeConstrainedProcessTracker tracker = repository.trackerOfProcessId(TestUtil.TEST_ID,
                ProcessTimedOutEvent.class.getName());
        repository.remove(tracker);
        Assert.assertTrue(repository.allTrackers(TestUtil.TEST_ID).isEmpty());
    }

    private TimeConstrainedProcessTracker tracker(int intervalSec) {
        TimeConstrainedProcessTracker tracker = new TimeConstrainedProcessTracker(
                TestUtil.TEST_ID,
                TestUtil.TEST_ID,
                TestUtil.TEST_ID,
                DateUtil.parse("2020-04-07"),
                new FixedRetryStrategy(new FixedRetryStrategy.Config()
                        .setIntervalSec(intervalSec)
                        .setId("FIXED_" + intervalSec)),
                ProcessTimedOutEvent.class.getName());
        tracker.informProcessTimedOut();
        return tracker;
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/time_tracker.sql"};
    }
}