package org.team4u.rl.infrastructure.persistence;

import org.team4u.base.config.ConfigService;
import org.team4u.base.serializer.HutoolJsonCacheSerializer;
import org.team4u.rl.domain.RateLimitConfigRepository;
import org.team4u.rl.domain.RateLimiterConfig;

/**
 * 基于json的配置资源库
 *
 * @author jay.wu
 */
public class JsonRateLimitConfigRepository implements RateLimitConfigRepository {

    private final ConfigService configService;

    public JsonRateLimitConfigRepository(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public RateLimiterConfig configOf(String configId) {
        String json = configService.get(configId);
        return HutoolJsonCacheSerializer.instance().deserialize(json, RateLimiterConfig.class);
    }
}