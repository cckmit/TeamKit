package org.team4u.kit.core.event.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.kit.core.event.api.Event;
import org.team4u.kit.core.event.api.EventStore;
import org.team4u.kit.core.log.LogMessage;

/**
 * 不保存事件源存储实现
 *
 * @author jay.wu
 */
public class TransientEventStore implements EventStore {

    private Log log = LogFactory.get();

    @Override
    public void store(Event event) {
        // 仅打印日志，不保存事件源
        log.info(LogMessage.create(this.getClass().getSimpleName(), "store")
                .append("eventClass", event.getClass().getSimpleName())
                .append("event", event.toString())
                .toString());
    }
}