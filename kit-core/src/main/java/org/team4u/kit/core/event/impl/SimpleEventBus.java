package org.team4u.kit.core.event.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.kit.core.event.api.Event;
import org.team4u.kit.core.event.api.EventBus;
import org.team4u.kit.core.event.api.EventListener;
import org.team4u.kit.core.event.api.EventStore;
import org.team4u.kit.core.log.LogMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 简单的进程内事件总线
 *
 * @author jay.wu
 */
public final class SimpleEventBus implements EventBus {

    private Log log = LogFactory.get();

    private EventStore eventStore;

    private List<EventListener> listeners = new ArrayList<EventListener>();

    public SimpleEventBus(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public List<EventListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    @Override
    public void register(EventListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));

        log.info(LogMessage.create(this.getClass().getSimpleName(), "register")
                .success()
                .append("listener", CollUtil.join(
                        Arrays.stream(listeners)
                                .map(it -> it.getClass().getSimpleName())
                                .collect(Collectors.toList()),
                        ","))
                .toString());
    }

    @Override
    public void unregister(EventListener... listeners) {
        this.listeners.removeAll(Arrays.asList(listeners));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void post(Event event) {
        eventStore.store(event);

        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}