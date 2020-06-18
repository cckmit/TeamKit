package org.team4u.ddd.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FakeTimeConstrainedProcessTrackerRepository implements TimeConstrainedProcessTrackerRepository {

    private final List<TimeConstrainedProcessTracker> trackers = new ArrayList<>();

    private final List<TimeConstrainedProcessTracker> removedTracker = new ArrayList<>();

    @Override
    public Collection<TimeConstrainedProcessTracker> allTimedOut(int sizeLimit) {
        return null;
    }

    @Override
    public Collection<TimeConstrainedProcessTracker> allTimedOutOf(String processTimedOutEventType, int sizeLimit) {
        return null;
    }

    @Override
    public Collection<TimeConstrainedProcessTracker> allTrackers(String processId) {
        return trackers.stream()
                .filter(it -> it.processId().equals(processId))
                .collect(Collectors.toList());
    }

    @Override
    public TimeConstrainedProcessTracker trackerOfProcessId(String processId, String processTimedOutEventType) {
        return trackers.stream()
                .filter(it -> it.processId().equals(processId))
                .filter(it -> it.processTimedOutEventType().equals(processTimedOutEventType))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(TimeConstrainedProcessTracker tracker) {
        TimeConstrainedProcessTracker existedTracker = trackerOfProcessId(tracker.processId(), tracker.processTimedOutEventType());
        if (existedTracker != null) {
            remove(tracker);
        }

        trackers.add(tracker);
    }

    @Override
    public void remove(TimeConstrainedProcessTracker tracker) {
        if (trackers.removeIf(it -> it.trackerId().equals(tracker.trackerId()))) {
            removedTracker.add(tracker);
        }
    }

    public List<TimeConstrainedProcessTracker> getTrackers() {
        return trackers;
    }

    public List<TimeConstrainedProcessTracker> getRemovedTracker() {
        return removedTracker;
    }
}