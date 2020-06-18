package org.team4u.ddd.process.retry.process;

import org.team4u.ddd.domain.model.FakeEvent1;
import org.team4u.ddd.infrastructure.serializer.FastJsonSerializer;
import org.team4u.ddd.process.FakeTimeConstrainedProcessTrackerRepository;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;
import org.team4u.ddd.process.retry.NeedRetryException;
import org.team4u.ddd.process.strategy.FakeRetryStrategyRepository;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.core.error.BusinessException;
import org.team4u.core.error.SystemException;
import org.team4u.ddd.TestUtil;

public class AbstractRetryableProcessCombinedEventSubscriberTest {

    @Test
    public void handleWithNormal() {
        TimeConstrainedProcessTrackerAppService trackerAppService = trackerAppService();
        SimpleRetryableProcessCombinedEventSubscriber subscriber = new SimpleRetryableProcessCombinedEventSubscriber(
                trackerAppService
        );

        FakeEvent1 e = new FakeEvent1(TestUtil.TEST_ID);
        subscriber.onEvent(e);

        Assert.assertEquals(subscriber.getFakeEvent1(), e);
        Assert.assertNull(subscriber.getTimedOutEvent());
        Assert.assertNull(trackerAppService.repository().trackerOfProcessId(
                TestUtil.TEST_ID,
                FakeRetryableProcessTimedOutEvent.class.getName())
        );
    }

    @Test
    public void handleWithTimedOut() {
        TimeConstrainedProcessTrackerAppService trackerAppService = trackerAppService();
        SimpleRetryableProcessCombinedEventSubscriber subscriber = new SimpleRetryableProcessCombinedEventSubscriber(
                trackerAppService
        );

        FakeEvent1 e1 = new FakeEvent1(TestUtil.TEST_ID);
        FakeRetryableProcessTimedOutEvent e = new FakeRetryableProcessTimedOutEvent(
                TestUtil.TEST_ID,
                FastJsonSerializer.instance().serialize(e1)
        );

        subscriber.onEvent(e);

        Assert.assertEquals(subscriber.getFakeEvent1().getDomainId(), TestUtil.TEST_ID);
        Assert.assertEquals(subscriber.getTimedOutEvent(), e);
        Assert.assertNull(trackerAppService().repository().trackerOfProcessId(
                TestUtil.TEST_ID,
                FakeRetryableProcessTimedOutEvent.class.getName())
        );
    }

    @Test
    public void handleWithSystemError() {
        TimeConstrainedProcessTrackerAppService trackerAppService = trackerAppService();

        ErrorRetryableProcessCombinedEventSubscriber subscriber = new ErrorRetryableProcessCombinedEventSubscriber(
                trackerAppService,
                new SystemException()
        );

        FakeEvent1 e = new FakeEvent1(TestUtil.TEST_ID);
        subscriber.onEvent(e);

        Assert.assertNotNull(trackerAppService.repository().trackerOfProcessId(
                TestUtil.TEST_ID,
                FakeRetryableProcessTimedOutEvent.class.getName())
        );
    }

    @Test
    public void handleWithBusinessError() {
        TimeConstrainedProcessTrackerAppService trackerAppService = trackerAppService();
        ErrorRetryableProcessCombinedEventSubscriber subscriber = new ErrorRetryableProcessCombinedEventSubscriber(
                trackerAppService,
                new BusinessException()
        );

        FakeEvent1 e = new FakeEvent1(TestUtil.TEST_ID);
        subscriber.onEvent(e);

        Assert.assertNull(trackerAppService.repository().trackerOfProcessId(
                TestUtil.TEST_ID,
                FakeRetryableProcessTimedOutEvent.class.getName())
        );
    }

    @Test
    public void handleWithNeedRetryError() {
        TimeConstrainedProcessTrackerAppService trackerAppService = trackerAppService();
        ErrorRetryableProcessCombinedEventSubscriber subscriber = new ErrorRetryableProcessCombinedEventSubscriber(
                trackerAppService,
                new NeedRetryException()
        );

        FakeEvent1 e = new FakeEvent1(TestUtil.TEST_ID);
        subscriber.onEvent(e);

        Assert.assertNotNull(trackerAppService.repository().trackerOfProcessId(
                TestUtil.TEST_ID,
                FakeRetryableProcessTimedOutEvent.class.getName())
        );
    }

    private TimeConstrainedProcessTrackerAppService trackerAppService() {
        FakeTimeConstrainedProcessTrackerRepository repository = new FakeTimeConstrainedProcessTrackerRepository();
        return new TimeConstrainedProcessTrackerAppService(repository, new FakeRetryStrategyRepository());
    }
}