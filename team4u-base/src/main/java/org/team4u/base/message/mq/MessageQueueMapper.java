package org.team4u.base.message.mq;

import cn.hutool.core.io.IoUtil;

import java.io.Closeable;

/**
 * 消息队列映射器
 *
 * @author jay.wu
 */
public class MessageQueueMapper implements Closeable {

    @SuppressWarnings("rawtypes")
    private final MessageQueue messageQueue;
    private final SimpleMessageHandler messageHandler;

    public MessageQueueMapper(MessageConsumer<?> messageConsumer,
                              MessageQueue<?> messageQueue,
                              MessageConverter messageConverter) {
        this.messageQueue = messageQueue;
        this.messageHandler = new SimpleMessageHandler(messageConsumer, messageConverter);

        start();
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

    public static class SimpleMessageHandler implements MessageHandler<Object> {

        @SuppressWarnings("rawtypes")
        private final MessageConsumer messageConsumer;
        private final MessageConverter messageConverter;

        private SimpleMessageHandler(MessageConsumer<?> messageConsumer, MessageConverter messageConverter) {
            this.messageConsumer = messageConsumer;
            this.messageConverter = messageConverter;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onMessage(Object message) {
            Object target = messageConverter.convert(message, messageConsumer.messageType());
            messageConsumer.onMessage(target);
        }
    }
}