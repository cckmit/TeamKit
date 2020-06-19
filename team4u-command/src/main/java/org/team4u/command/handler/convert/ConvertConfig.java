package org.team4u.command.handler.convert;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.command.handler.HandlerConfig;

import java.util.Map;

public class ConvertConfig extends HandlerConfig {

    private String targetType;

    public String getTargetType() {
        return targetType;
    }

    public ConvertConfig setTargetType(String targetType) {
        this.targetType = targetType;
        return this;
    }

    public Class<?> loadTargetType() {
        if (StrUtil.isEmpty(getTargetType())) {
            return Map.class;
        }

        return ClassUtil.loadClass(getTargetType());
    }
}