package org.team4u.ddd.infrastructure.persistence.mybatis;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.team4u.base.error.IdempotentException;
import org.team4u.ddd.domain.model.DomainEventAwareRepository;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.process.TimeConstrainedProcessTracker;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerRepository;
import org.team4u.ddd.process.strategy.RetryStrategyRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于Mybatis实现的超时跟踪器资源库
 *
 * @author jay.wu
 */
public class MybatisTimeConstrainedProcessTrackerRepository
        extends DomainEventAwareRepository<TimeConstrainedProcessTracker>
        implements TimeConstrainedProcessTrackerRepository {

    private final TimeConstrainedProcessTrackerMapper mapper;
    private final RetryStrategyRepository retryStrategyRepository;

    public MybatisTimeConstrainedProcessTrackerRepository(EventStore eventStore,
                                                          TimeConstrainedProcessTrackerMapper mapper,
                                                          RetryStrategyRepository retryStrategyRepository) {
        super(eventStore);
        this.mapper = mapper;
        this.retryStrategyRepository = retryStrategyRepository;
    }

    @Override
    public Collection<TimeConstrainedProcessTracker> allTimedOut(int sizeLimit) {
        return toTrackers(
                mapper.selectPage(
                        new Page<>(1, sizeLimit),
                        withTimeoutCriteria(new LambdaQueryWrapper<>())
                ).getRecords()
        );
    }

    @Override
    public Collection<TimeConstrainedProcessTracker> allTimedOutOf(String processTimedOutEventType, int sizeLimit) {
        return toTrackers(
                mapper.selectPage(
                        new Page<>(1, sizeLimit),
                        withTimeoutCriteria(new LambdaQueryWrapper<>()).eq(
                                TimeConstrainedProcessTrackerEntity::getProcessTimedOutEventType,
                                processTimedOutEventType
                        )
                ).getRecords()
        );
    }

    @Override
    public Collection<TimeConstrainedProcessTracker> allTrackers(String processId) {
        return toTrackers(mapper.selectList(
                new LambdaQueryWrapper<TimeConstrainedProcessTrackerEntity>()
                        .eq(TimeConstrainedProcessTrackerEntity::getProcessId, processId))
        );
    }

    @Override
    public TimeConstrainedProcessTracker trackerOfProcessId(String processId, String processTimedOutEventType) {
        return toTracker(
                mapper.selectOne(
                        new LambdaQueryWrapper<TimeConstrainedProcessTrackerEntity>()
                                .eq(TimeConstrainedProcessTrackerEntity::getProcessId, processId)
                                .eq(TimeConstrainedProcessTrackerEntity::getProcessTimedOutEventType,
                                        processTimedOutEventType)
                )
        );
    }

    @Override
    protected void doSave(TimeConstrainedProcessTracker tracker) {
        TimeConstrainedProcessTrackerEntity entity = toEntity(tracker);

        entity.setUpdateTime(new Date());

        if (entity.getId() == null) {
            entity.setCreateTime(new Date());
            try {
                mapper.insert(entity);
            } catch (DuplicateKeyException e) {
                throw new IdempotentException(String.format(
                        "processId=%s|processTimedOutEventType=%s",
                        tracker.processId(),
                        tracker.processTimedOutEventType()
                ), e);
            }
        } else {
            if (mapper.updateById(entity) == 0) {
                tracker.failWhenConcurrencyViolation();
            }
        }

        tracker.setConcurrencyVersion(entity.getVersion());
    }

    @Override
    public void remove(TimeConstrainedProcessTracker tracker) {
        mapper.deleteById(tracker.getId());
    }

    private TimeConstrainedProcessTrackerEntity toEntity(TimeConstrainedProcessTracker tracker) {
        TimeConstrainedProcessTrackerEntity entity = new TimeConstrainedProcessTrackerEntity();
        entity.setId(tracker.getId());
        entity.setCompleted(tracker.isCompleted());
        entity.setDescription(tracker.description());
        entity.setProcessId(tracker.processId());
        entity.setProcessInformedOfTimeout(tracker.isProcessInformedOfTimeout());
        entity.setRetryCount(tracker.retryCount());
        entity.setProcessTimedOutEventType(tracker.processTimedOutEventType());
        entity.setTimeoutOccursOn(tracker.timeoutOccursOn());
        entity.setTrackerId(tracker.trackerId());
        entity.setVersion(tracker.concurrencyVersion());
        entity.setRetryStrategyId(tracker.retryStrategy().strategyId());
        return entity;
    }

    private LambdaQueryWrapper<TimeConstrainedProcessTrackerEntity> withTimeoutCriteria(
            LambdaQueryWrapper<TimeConstrainedProcessTrackerEntity> wrapper) {
        return wrapper.eq(TimeConstrainedProcessTrackerEntity::getCompleted, false)
                .eq(TimeConstrainedProcessTrackerEntity::getProcessInformedOfTimeout, false)
                .lt(TimeConstrainedProcessTrackerEntity::getTimeoutOccursOn, new Date())
                .orderByAsc(TimeConstrainedProcessTrackerEntity::getTimeoutOccursOn);
    }

    private TimeConstrainedProcessTracker toTracker(TimeConstrainedProcessTrackerEntity entity) {
        if (entity == null) {
            return null;
        }

        TimeConstrainedProcessTracker tracker = new TimeConstrainedProcessTracker(
                entity.getTrackerId(),
                entity.getProcessId(),
                entity.getDescription(),
                entity.getCreateTime(),
                retryStrategyRepository.strategyOf(entity.getRetryStrategyId()),
                entity.getProcessTimedOutEventType()
        );

        BeanUtil.copyProperties(entity, tracker);
        tracker.setConcurrencyVersion(entity.getVersion());
        return tracker;
    }

    private List<TimeConstrainedProcessTracker> toTrackers(List<TimeConstrainedProcessTrackerEntity> entities) {
        return entities.stream()
                .map(this::toTracker)
                .collect(Collectors.toList());
    }

    @Override
    public TimeConstrainedProcessTracker domainOf(String domainId) {
        TimeConstrainedProcessTrackerEntity entity = mapper.selectOne(
                new LambdaQueryWrapper<TimeConstrainedProcessTrackerEntity>()
                        .eq(TimeConstrainedProcessTrackerEntity::getId, domainId));
        return toTracker(entity);
    }
}