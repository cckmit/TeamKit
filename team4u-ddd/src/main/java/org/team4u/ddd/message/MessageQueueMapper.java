package org.team4u.ddd.message;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.func.VoidFunc1;

import java.io.Closeable;

/**
 * 消息队列映射器
 *
 * @author jay.wu
 */
public class MessageQueueMapper implements Closeable {

    @SuppressWarnings("rawtypes")
    private final MessageQueue messageQueue;
    private final MessageHandler messageHandler;

    public MessageQueueMapper(MessageConsumer<?> messageConsumer,
                              MessageQueue<?> messageQueue,
                              MessageConverter messageConverter) {
        this.messageQueue = messageQueue;
        this.messageHandler = new MessageHandler(messageConsumer, messageConverter);
    }

    @SuppressWarnings("unchecked")
    public void start() {
        messageQueue.messageHandler(messageHandler);
        messageQueue.start();
    }

    @Override
    public void close() {
        IoUtil.close(messageQueue);
    }

    private static class MessageHandler implements VoidFunc1<Object> {

        @SuppressWarnings("rawtypes")
        private final MessageConsumer messageConsumer;
        private final MessageConverter messageConverter;

        private MessageHandler(MessageConsumer<?> messageConsumer, MessageConverter messageConverter) {
            this.messageConsumer = messageConsumer;
            this.messageConverter = messageConverter;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void call(Object message) {
            Object target = messageConverter.convert(message, messageConsumer.messageType());
            messageConsumer.processMessage(target);
        }
    }
}