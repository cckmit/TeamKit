package org.team4u.ddd.message;

import cn.hutool.core.convert.Converter;
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
                              Converter<?> converter) {
        this.messageQueue = messageQueue;
        this.messageHandler = new MessageHandler(messageConsumer, converter);
    }

    @SuppressWarnings("unchecked")
    public void start() {
        this.messageQueue.messageHandler(messageHandler);
        messageQueue.start();
    }

    @Override
    public void close() {
        IoUtil.close(messageQueue);
    }

    private static class MessageHandler implements VoidFunc1<Object> {

        @SuppressWarnings("rawtypes")
        private final MessageConsumer messageConsumer;
        private final Converter<?> converter;

        private MessageHandler(MessageConsumer<?> messageConsumer, Converter<?> converter) {
            this.messageConsumer = messageConsumer;
            this.converter = converter;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void call(Object message) {
            Object target = converter.convert(message, null);
            messageConsumer.processMessage(target);
        }
    }
}