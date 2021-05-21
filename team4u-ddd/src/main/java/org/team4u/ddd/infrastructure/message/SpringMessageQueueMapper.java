package org.team4u.ddd.infrastructure.message;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.team4u.ddd.message.MessageConsumer;
import org.team4u.ddd.message.MessageConverter;
import org.team4u.ddd.message.MessageQueue;
import org.team4u.ddd.message.MessageQueueMapper;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay.wu
 */
public class SpringMessageQueueMapper implements ApplicationContextAware, Closeable {

    private final List<MessageQueueMapper> mappers = new ArrayList<>();

    private ApplicationContext applicationContext;

    public SpringMessageQueueMapper map(Class<? extends MessageConsumer<?>> messageConsumerType,
                                        MessageQueue<?> messageQueue,
                                        Class<? extends MessageConverter> messageConverterType) {
        mappers.add(
                new MessageQueueMapper(
                        applicationContext.getBean(messageConsumerType),
                        messageQueue,
                        applicationContext.getBean(messageConverterType)
                )
        );
        return this;
    }

    public void start() {
        mappers.forEach(MessageQueueMapper::start);
    }

    @Override
    public void close() {
        mappers.forEach(MessageQueueMapper::close);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
