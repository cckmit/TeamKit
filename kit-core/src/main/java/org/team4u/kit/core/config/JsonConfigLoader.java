package org.team4u.kit.core.config;

import com.jsoniter.JsonIterator;
import com.xiaoleilu.hutool.util.ReUtil;
import org.team4u.kit.core.util.AssertUtil;

public class JsonConfigLoader implements ConfigLoader<String> {

    public static final JsonConfigLoader INSTANCE = new JsonConfigLoader();

    @Override
    public <T> T load(Class<T> configClass, String content) {
        content = ReUtil.get(".*?(\\{.+\\}).*", content, 1);
        AssertUtil.notNull(content);
        content = content.replaceAll("(?m)^\\s*//.*", "");
        return JsonIterator.deserialize(content, configClass);
    }

    @Override
    public String getKey() {
        return Configurer.LoaderType.JSON.getKey();
    }
}