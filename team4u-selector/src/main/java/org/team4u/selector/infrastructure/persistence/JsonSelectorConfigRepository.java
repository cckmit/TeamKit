package org.team4u.selector.infrastructure.persistence;

import org.team4u.base.config.AbstractJsonCacheableConfigRepository;
import org.team4u.base.config.ConfigService;
import org.team4u.selector.domain.selector.SelectorConfig;
import org.team4u.selector.domain.selector.SelectorConfigRepository;

/**
 * 基于json的配置资源库
 *
 * @author jay.wu
 */
public class JsonSelectorConfigRepository
        extends AbstractJsonCacheableConfigRepository<SelectorConfig>
        implements SelectorConfigRepository {

    public JsonSelectorConfigRepository(ConfigService configService) {
        super(configService);
    }
}