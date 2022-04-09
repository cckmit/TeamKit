package org.team4u.command.domain.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Pair;
import org.team4u.base.config.IdentifiedConfig;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.lang.lazy.LazyValueFormatter;

import java.util.Map;

/**
 * 命令配置
 *
 * @author jay.wu
 */
public class CommandConfig extends IdentifiedConfig {

    private final LazyFunction<Pair<String, Class<?>>, Object> itemCache = LazyFunction.of(
            LazyFunction.Config.builder()
                    .name(this.getClass().getSimpleName())
                    .parameterFormatter(cacheKeyFormatter())
                    .build(),
            this::itemOf
    );

    /**
     * 命令配置明细项
     * <p>
     * key = 类型
     * value = 配置集合
     */
    private final Map<String, Dict> items;

    public CommandConfig(Map<String, Dict> items) {
        this.items = items;
    }

    /**
     * 获取某个类型的配置
     *
     * @param itemId     配置标识
     * @param configType 配置类型
     * @param <T>        配置类型
     * @return 配置对象
     */
    @SuppressWarnings("unchecked")
    public <T> T itemOf(String itemId, Class<T> configType) {
        return (T) itemCache.apply(Pair.of(itemId, configType));
    }

    /**
     * 获取某个类型的配置
     *
     * @param itemId 配置标识
     * @return 配置对象
     */
    public Dict itemOf(String itemId) {
        return items.get(itemId);
    }

    private LazyValueFormatter<Pair<String, Class<?>>> cacheKeyFormatter() {
        return (log, value) -> value.getKey();
    }

    @SuppressWarnings("unchecked")
    private <T> T itemOf(Pair<String, Class<?>> itemIdAndType) {
        return (T) BeanUtil.toBean(itemOf(itemIdAndType.getKey()), itemIdAndType.getValue());
    }
}