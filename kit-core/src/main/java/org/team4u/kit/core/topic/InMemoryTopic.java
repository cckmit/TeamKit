package org.team4u.kit.core.topic;

import cn.hutool.core.util.RandomUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTopic<M> implements Topic<M> {

    protected Map<String, Subscriber<M>> subscriberMap = new ConcurrentHashMap<String, Subscriber<M>>();

    @Override
    public void publish(M m) {
        if (subscriberMap.isEmpty()) {
            return;
        }

        List<Subscriber<M>> keys = new ArrayList<Subscriber<M>>(subscriberMap.values());
        for (Subscriber<M> listener : keys) {
            listener.onMessage(m);
        }
    }

    @Override
    public String register(Subscriber<M> listener) {
        String id = RandomUtil.randomUUID();
        subscriberMap.put(id, listener);
        return id;
    }

    @Override
    public void unregister(String id) {
        subscriberMap.remove(id);
    }

    @Override
    public void unregisterAll() {
        subscriberMap.clear();
    }

    @Override
    public void close() throws IOException {
        unregisterAll();
    }
}