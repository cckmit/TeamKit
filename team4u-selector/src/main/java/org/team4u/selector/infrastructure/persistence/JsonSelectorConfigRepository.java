package org.team4u.selector.infrastructure.persistence;

import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.base.config.ConfigService;
import org.team4u.base.lang.CacheableFunc1;
import org.team4u.selector.domain.selector.SelectorConfig;
import org.team4u.selector.domain.selector.SelectorConfigRepository;

/**
 * 基于json的配置资源库
 *
 * @author jay.wu
 */
public class JsonSelectorConfigRepository implements SelectorConfigRepository {

    private final ConfigService configService;

    public JsonSelectorConfigRepository(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public SelectorConfig selectorConfigOfId(String id) {
        String json = configService.get(id);

        if (StrUtil.isEmpty(json)) {
            return null;
        }

        return new CacheableFunc1<String, SelectorConfig>(CacheUtil.newLRUCache(1000)) {
            @Override
            public SelectorConfig call(String parameter) {
                return JSONUtil.toBean(parameter, SelectorConfig.class);
            }
        }.callWithCacheAndRuntimeException(json);
    }
}