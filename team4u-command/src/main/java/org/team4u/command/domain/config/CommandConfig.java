package org.team4u.command.domain.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;

import java.util.Map;

public class CommandConfig {

    private final Map<String, Dict> items;

    public CommandConfig(Map<String, Dict> items) {
        this.items = items;
    }

    public <T> T itemOf(String itemId, Class<T> itemType) {
        return BeanUtil.toBean(items.get(itemId), itemType);
    }
}
