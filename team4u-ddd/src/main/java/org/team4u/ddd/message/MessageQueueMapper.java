package org.team4u.ddd.message;

import cn.hutool.core.convert.Converter;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.func.VoidFunc1;

import java.io.Closeable;

public class MessageQueueMapper implements Closeable {

    @SuppressWarnings("rawtypes")
    private final MessageConsumer messageConsumer;
    @SuppressWarnings("rawtypes")
    private final MessageQueue messageQueue;
    private final Converter<?> converter;

    public MessageQueueMapper(MessageConsumer<?> messageConsumer,
                              MessageQueue<?> messageQueue,
                              Converter<?> converter) {
        this.messageConsumer = messageConsumer;
        this.messageQueue = messageQueue;
        this.converter = converter;
    }

    @SuppressWarnings("unchecked")
    public void start() {
        messageQueue.messageHandler(new MessageHandler());
        messageQueue.start();
    }

    @Override
    public void close() {
        IoUtil.close(messageQueue);
    }

    private class MessageHandler implements VoidFunc1<Object> {

        @SuppressWarnings("unchecked")
        @Override
        public void call(Object message) throws Exception {
            Object m = converter.convert(message, null);
            messageConsumer.processMessage(m);
        }
    }
}