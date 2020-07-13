package org.team4u.ddd.domain.model;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.base.log.LogService;

/**
 * 抽象多事件处理器
 * <p>
 * 事件总线在接收到一个领域事件时，先调用supports(event)方法判断当前监听器是否支持该事件，
 * 如果支持则调用onEvent()方法处理该事件
 *
 * @author jay.wu
 */
public abstract class AbstractDomainEventSubscriber implements DomainEventSubscriber<DomainEvent> {

    protected final Log log = LogFactory.get();

    @Override
    public void onEvent(DomainEvent event) {
        if (!supports(event)) {
            return;
        }

        LogMessage lm = LogMessages.createWithMasker(this.getClass().getSimpleName(), "handle")
                .append("event", event);

        try {
            handle(event);
            log.info(lm.success().toString());
        } catch (Throwable e) {
            LogService.logForError(log, lm, e);
        }
    }

    /**
     * 处理事件
     *
     * @param event 要处理的事件
     */
    protected abstract void handle(DomainEvent event) throws Throwable;

    /**
     * 判断是否监听指定的事件类型
     */
    protected abstract boolean supports(DomainEvent event);
}