package org.team4u.base.message.mq;

/**
 * 消息转换器
 *
 * @author jay.wu
 */
public interface MessageConverter {

    /**
     * 转换
     *
     * @param value      原始值
     * @param targetType 目标类型
     * @param <Target>   目标类型
     * @return 目标值
     */
    <Target> Target convert(Object value, Class<Target> targetType);
}