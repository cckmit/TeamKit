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

    private static DynamicMasker dynamicMasker = new DynamicMasker(
            new FastJsonDynamicMaskerValueSerializer(),
            new LocalDynamicMaskerConfigRepository()
    );

    /**
     * 创建包含掩码服务的日志消息
     */
    public static LogMessage createWithMasker(String module, String eventName) {
        LogMessage lm = LogMessage.create(module, eventName);
        lm.config().setLogMessageRender(new MaskLogMessageRender(dynamicMasker));
        return lm;
    }

    /**
     * 创建日志消息
     */
    public static LogMessage create(String module, String eventName) {
        return LogMessage.create(module, eventName);
    }

    public static void setDynamicMasker(DynamicMasker dynamicMasker) {
        LogMessages.dynamicMasker = dynamicMasker;
    }
}