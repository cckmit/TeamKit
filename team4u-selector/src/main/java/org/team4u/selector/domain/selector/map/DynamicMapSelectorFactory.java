package org.team4u.selector.domain.selector.map;

import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.SelectorFactory;

/**
 * 动态映射选择器构建工厂
 *
 * @author jay.wu
 */
public class DynamicMapSelectorFactory implements SelectorFactory {

    public static DynamicMapSelector.Config toConfig(String jsonConfig) {
        return JSONUtil.toBean(
                jsonConfig,
                DynamicMapSelector.Config.class);
    }

    @Override
    public Selector create(String jsonConfig) {
        return new DynamicMapSelector(toConfig(jsonConfig));
    }

    @Override
    public String id() {
        return "dynamicMap";
    }
}