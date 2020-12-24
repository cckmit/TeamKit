package org.team4u.selector.domain.selector.entity.whitelist;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.selector.domain.selector.entity.Selector;
import org.team4u.selector.domain.selector.entity.binding.SelectorBinding;
import org.team4u.selector.domain.selector.entity.binding.SimpleMapBinding;

import java.util.List;
import java.util.Map;

/**
 * 白名单选择器
 *
 * @author jay.wu
 */
public class WhitelistSelector implements Selector {

    public final static String MATCH = "MATCH";
    public final static String ANY = "*";

    private final List<NameListItem> config;

    public WhitelistSelector(List<NameListItem> config) {
        this.config = config;
    }

    /**
     * 选择
     *
     * @return 若命中则返回常量MATCH，负责为常量NONE
     */
    @Override
    public String select(SelectorBinding binding) {
        Map.Entry<String, Object> value = value(binding);

        if (keyOf(value)) {
            return MATCH;
        }

        return NONE;
    }

    private boolean keyOf(Map.Entry<String, Object> value) {
        return config.stream()
                .filter(it -> StrUtil.equals(it.getKey(), value.getKey()))
                .findFirst()
                .map(it -> matchUserId(it, value.getValue()))
                // 无法匹配key，则尝试查找*
                .orElseGet(() -> config.stream()
                        .filter(it -> StrUtil.equals(it.getKey(), ANY))
                        .anyMatch(it -> matchUserId(it, value.getValue()))
                );
    }

    private boolean matchUserId(NameListItem it, Object userId) {
        return CollUtil.contains(it.getUserIdList(), userId) ||
                CollUtil.contains(it.getUserIdList(), ANY);
    }

    private Map.Entry<String, Object> value(SelectorBinding binding) {
        if (binding instanceof SimpleMapBinding) {
            return CollUtil.getFirst(((SimpleMapBinding) binding).entrySet());
        }

        return null;
    }

    public static class NameListItem {
        private final String key;

        private final List<Object> userIdList;

        public NameListItem(String key, List<Object> userIdList) {
            this.key = key;
            this.userIdList = userIdList;
        }

        public String getKey() {
            return key;
        }

        public List<Object> getUserIdList() {
            return userIdList;
        }
    }
}