package org.team4u.base.bean.event;

import org.team4u.base.message.jvm.MessageSubscriber;
import org.team4u.base.spring.SpringInitializedPublisher;

/**
 * 应用启动完成事件
 * <p>
 * - spring环境需要提前注入SpringInitializedPublisher
 * <p>
 * - 非spring环境需要手工注册订阅着，并且手工发布事件
 *
 * <code>
 * MessagePublisher.instance().subscribe(subscriber);
 * MessagePublisher.instance().publish(new ApplicationInitializedEvent());
 * </code>
 *
 * @author jay.wu
 * @see MessageSubscriber
 * @see SpringInitializedPublisher
 */
public class ApplicationInitializedEvent {
}