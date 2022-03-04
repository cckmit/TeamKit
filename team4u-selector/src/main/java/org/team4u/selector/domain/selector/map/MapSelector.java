package org.team4u.selector.domain.selector.map;

import cn.hutool.core.util.ReUtil;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.binding.SelectorBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;

import java.util.Map;
import java.util.Optional;

/**
 * 映射选择器
 *
 * @author jay.wu
 */
public class MapSelector implements Selector {

    private final Map<String, String> config;

    public MapSelector(Map<String, String> config) {
        this.config = config;
    }

    @Override
    public String select(SelectorBinding binding) {
        String key = ((SingleValueBinding) binding).value();
        // 优先匹配精确key，其次匹配符合正则表达式的key，若均无法找到则返回NONE
        return Optional.ofNullable(config.get(key))
                .orElseGet(() -> config.entrySet()
                        .stream()
                        .filter(it -> ReUtil.isMatch(it.getKey(), key))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .orElse(NONE)
                );
    }
}