package org.team4u.rl.infrastructure.persistence;

import cn.hutool.cache.CacheUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.base.config.ConfigService;
import org.team4u.base.lang.CacheableFunc1;
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

        return new CacheableFunc1<String, RateLimiterConfig>(CacheUtil.newLRUCache(1000)) {
            @Override
            public RateLimiterConfig call(String parameter) {
                return JSONUtil.toBean(parameter, RateLimiterConfig.class);
            }
        }.callWithCacheAndRuntimeException(json);
    }
}