package org.team4u.base.log;

import org.team4u.base.masker.dynamic.DynamicMasker;
import org.team4u.base.masker.dynamic.FastJsonDynamicMaskerValueSerializer;
import org.team4u.base.masker.dynamic.LocalDynamicMaskerConfigRepository;

/**
 * 日志消息服务类
 *
 * @author jay.wu
 */
public class LogMessages {

    /**
     * 创建包含掩码服务的日志消息
     */
    public static LogMessage createWithMasker(String module, String eventName) {
        LogMessage lm = LogMessage.create(module, eventName);

        lm.config().setLogMessageRender(
                new MaskLogMessageRender(
                        new DynamicMasker(
                                new FastJsonDynamicMaskerValueSerializer(),
                                new LocalDynamicMaskerConfigRepository()
                        )
                )
        );

        return lm;
    }

    /**
     * 创建日志消息
     */
    public static LogMessage create(String module, String eventName) {
        return LogMessage.create(module, eventName);
    }
}