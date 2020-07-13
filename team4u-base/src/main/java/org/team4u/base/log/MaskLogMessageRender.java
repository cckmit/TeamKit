package org.team4u.base.log;

import com.alibaba.fastjson.JSON;
import org.team4u.base.masker.dynamic.DynamicMasker;

import java.util.Map;


/**
 * 掩码日志消息渲染器
 *
 * @author jay.wu
 */
public class MaskLogMessageRender extends SimpleLogMessageRender {

    private final DynamicMasker masker;

    public MaskLogMessageRender(DynamicMasker masker) {
        this.masker = masker;
    }

    @Override
    public String render(LogMessage logMessage) {
        masker.context().setConfigId(logMessage.getModule() + "." + logMessage.getEventName());
        return super.render(logMessage);
    }

    @Override
    protected void renderBody(LogMessage logMessage, LogStringBuilder builder) {
        String value = masker.mask(logMessage.fieldValues());

        for (Map.Entry<String, Object> entry : JSON.parseObject(value).entrySet()) {
            builder.appendWithSeparator(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }
    }
}