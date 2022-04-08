package org.team4u.base.message.jvm;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;

import java.util.Collections;
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

    private final Set<MessageSubscriber<?>> subscribers = new ConcurrentHashSet<>();

    public static MessagePublisher instance() {
        return INSTANCE;
    }

    /**
     * 注册订阅者集合
     *
     * @param subscribers 订阅者集合
     */
    public void subscribe(List<? extends MessageSubscriber<?>> subscribers) {
        subscribers.forEach(this::subscribe);
    }

    /**
     * 注册订阅者
     *
     * @param subscriber 订阅者
     */
    public void subscribe(MessageSubscriber<?> subscriber) {
        if (subscribers.add(subscriber)) {
            log.info(LogMessage.create(this.getClass().getSimpleName(), "subscribe")
                    .success()
                    .append("subscriber", subscriber.getClass().getSimpleName())
                    .toString());
        }
    }

    /**
     * 发布消息
     *
     * @param message 消息
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void publish(Object message) {
        for (MessageSubscriber subscriber : subscribers) {
            if (subscriber.supports(message)) {
                subscriber.onMessage(message);
            }
        }
    }

    /**
     * 获取订阅者集合
     *
     * @return 订阅者集合
     */
    public Set<MessageSubscriber<?>> subscribers() {
        return Collections.unmodifiableSet(subscribers);
    }

    public void reset() {
        subscribers.clear();
    }
}