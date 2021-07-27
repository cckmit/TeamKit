package org.team4u.rl.infrastructure.persistence;

import org.team4u.base.config.AbstractJsonConfigRepository;
import org.team4u.base.config.ConfigService;
import org.team4u.rl.domain.RateLimitConfigRepository;
import org.team4u.rl.domain.RateLimiterConfig;

/**
 * 基于json的配置资源库
 *
 * @author jay.wu
 */
public class JsonRateLimitConfigRepository
        extends AbstractJsonConfigRepository<RateLimiterConfig>
        implements RateLimitConfigRepository {

    public JsonRateLimitConfigRepository(ConfigService configService) {
        super(configService);
    }
}