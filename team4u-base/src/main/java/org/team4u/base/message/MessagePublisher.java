package org.team4u.base.message;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 消息发布者
 *
 * @author jay.wu
 */
public class MessagePublisher {

    private final Log log = LogFactory.get();

    private static final MessagePublisher INSTANCE = new MessagePublisher();

    private final Set<MessageSubscriber<?>> subscribers = new HashSet<>();

    public static MessagePublisher instance() {
        return INSTANCE;
    }

    public void subscribe(List<? extends MessageSubscriber<?>> listeners) {
        listeners.forEach(this::subscribe);
    }

    public void subscribe(MessageSubscriber<?> subscriber) {
        subscribers.add(subscriber);

        log.info(LogMessage.create(this.getClass().getSimpleName(), "subscribe")
                .success()
                .append("subscriber", subscriber.getClass().getSimpleName())
                .toString());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void publish(Object event) {
        for (MessageSubscriber subscriber : subscribers) {
            subscriber.onMessage(event);
        }
    }

    public Set<MessageSubscriber<?>> subscribers() {
        return Collections.unmodifiableSet(subscribers);
    }
}