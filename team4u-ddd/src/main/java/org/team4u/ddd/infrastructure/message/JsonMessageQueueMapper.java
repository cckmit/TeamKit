package org.team4u.ddd.infrastructure.message;

import org.team4u.ddd.message.MessageConsumer;
import org.team4u.ddd.message.MessageQueue;
import org.team4u.ddd.message.MessageQueueMapper;

/**
 * 基于JSON的消息队列映射器
 *
 * @author jay.wu
 */
public class JsonMessageQueueMapper extends MessageQueueMapper {

    public JsonMessageQueueMapper(MessageConsumer<?> messageConsumer,
                                  MessageQueue<?> messageQueue) {
        super(messageConsumer, messageQueue, new JsonMessageConverter());
    }
}