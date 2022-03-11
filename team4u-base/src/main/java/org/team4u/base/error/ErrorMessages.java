package org.team4u.base.error;

import cn.hutool.core.util.StrUtil;
import org.team4u.base.config.ConfigService;

public class ErrorMessages {

    private final ConfigService configService;

    public ErrorMessages(ConfigService configService) {
        this.configService = configService;
    }

    public String message(String key, Object... params) {
        String value = configService.get(key);
        if (value != null) {
            return key + " - " + StrUtil.format(value, params);
        } else {
            return StrUtil.format(key, params);
        }
    }
}