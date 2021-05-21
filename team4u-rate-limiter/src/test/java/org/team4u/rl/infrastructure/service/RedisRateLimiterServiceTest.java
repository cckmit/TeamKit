package org.team4u.rl.infrastructure.service;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.team4u.rl.domain.RateLimiterService;
import redis.clients.jedis.JedisPoolConfig;

public class RedisRateLimiterServiceTest extends AbstractRateLimiterServiceTest {

    private final RedisRateLimiterService service = new RedisRateLimiterService(
            new AbstractRateLimiterService.Config().setConfigId("config/test"),
            rateLimitConfigRepository(),
            createRedisTemplate()
    );

    @Override
    protected RateLimiterService newRateLimiterService() {
        return service;
    }

    private StringRedisTemplate createRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();

        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(new JedisPoolConfig());
        factory.setHostName("10.199.244.210");
        factory.setPort(6379);
        factory.afterPropertiesSet();
        template.setConnectionFactory(factory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}