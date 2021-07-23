package org.team4u.selector.infrastructure.persistence;

import cn.hutool.json.JSONUtil;
import org.team4u.base.config.ConfigService;
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
        return JSONUtil.toBean(json, SelectorConfig.class);
    }
}