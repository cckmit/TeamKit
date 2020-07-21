package org.team4u.ddd.domain.model;

import org.team4u.ddd.message.AbstractMessageConsumer;

import java.util.concurrent.ExecutorService;

/**
 * 抽象单一事件处理器
 *
 * @param <E> 事件类型
 * @author jay.wu
 */
public abstract class AbstractSingleDomainEventSubscriber<E extends DomainEvent> extends AbstractMessageConsumer<E> {


    public AbstractSingleDomainEventSubscriber(ExecutorService executorService) {
        super(executorService);
    }

    public AbstractSingleDomainEventSubscriber() {
        this(null);
    }
}