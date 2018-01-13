package org.team4u.kit.core.config;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;

public class JsonConfigLoader implements ConfigLoader<String> {

    public static final JsonConfigLoader INSTANCE = new JsonConfigLoader();

    @Override
    public <T> T load(Class<T> configClass, String content) {
        content = ReUtil.get(".*?(\\{.+\\}).*", content, 1);
        Assert.notNull(content);
        content = content.replaceAll("(?m)^\\s*//.*", "");
        return JSON.parseObject(content, configClass);
    }

    @Override
    public String getKey() {
        return Configurer.LoaderType.JSON.getKey();
    }
}