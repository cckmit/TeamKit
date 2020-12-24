package org.team4u.selector.domain.selector.entity.whitelist;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.entity.Selector;
import org.team4u.selector.domain.selector.entity.SelectorFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 白名单选择器构建工厂
 *
 * @author jay.wu
 */
public class WhitelistSelectorFactory implements SelectorFactory {

    @SuppressWarnings("unchecked")
    @Override
    public Selector create(String jsonConfig) {
        List<Dict> nameListItems = JSONUtil.toList(
                JSONUtil.parseArray(jsonConfig),
                Dict.class
        );

        return new WhitelistSelector(nameListItems.stream()
                .map(it -> {
                    Map.Entry<String, Object> entry = CollUtil.getFirst(it.entrySet());
                    return new WhitelistSelector.NameListItem(entry.getKey(), (List<Object>) entry.getValue());
                })
                .collect(Collectors.toList()));
    }

    @Override
    public String id() {
        return "whitelist";
    }
}