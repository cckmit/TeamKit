package org.team4u.id.domain.seq.value.cache.queue;

import lombok.Getter;

/**
 * 序号队列状态
 *
 * @author jay.wu
 */
public enum SequenceQueueStatus {

    CREATED,
    EXHAUSTED;

    @Getter
    private final long occurredOn = System.currentTimeMillis();
}