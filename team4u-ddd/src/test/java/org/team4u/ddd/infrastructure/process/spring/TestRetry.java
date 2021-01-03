package org.team4u.ddd.infrastructure.process.spring;

import org.springframework.stereotype.Service;
import org.team4u.base.error.BusinessException;
import org.team4u.ddd.process.retry.method.annotation.Retryable;
import org.team4u.ddd.process.retry.method.interceptor.RetryableMethodTimedOutEvent;

import java.util.List;
import java.util.Map;

@Service
public class TestRetry {

    private String name;
    private Map<String, Object> dict;
    private List<Integer> ages;
    private RetryableMethodTimedOutEvent event;

    @Retryable(
            retryStrategyId = "1",
            shouldRemoveTrackerAfterCompleted = true
    )
    public void handle(String name, Map<String, Object> dict, List<Integer> ages) {
        this.name = name;
        this.dict = dict;
        this.ages = ages;
        this.event = RetryableMethodTimedOutEvent.currentEvent();
    }

    private void error() {
        throw new BusinessException();
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getDict() {
        return dict;
    }

    public List<Integer> getAges() {
        return ages;
    }

    public RetryableMethodTimedOutEvent getEvent() {
        return event;
    }

    public void reset() {
        name = null;
        dict = null;
        ages = null;
        event = null;
    }
}
