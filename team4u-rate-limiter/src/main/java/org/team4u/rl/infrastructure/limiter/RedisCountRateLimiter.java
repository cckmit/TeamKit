package org.team4u.rl.infrastructure.limiter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.team4u.base.log.LogMessage;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;
import org.team4u.rl.domain.RateLimiterFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的计数限流器
 *
 * @author jay.wu
 */
public class RedisCountRateLimiter implements RateLimiter {

    private final Log log = LogFactory.get();

    @Getter
    private final RateLimiterConfig config;
    @Getter
    private final RedisTemplate<String, String> redisTemplate;

    public RedisCountRateLimiter(RateLimiterConfig config, RedisTemplate<String, String> redisTemplate) {
        this.config = config;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean tryAcquire(String key) {
        if (!canAcquire(key)) {
            return false;
        }

        try {
            long count = ObjectUtil.defaultIfNull(redisTemplate.opsForValue().increment(key, 1), 0L);

            expireKey(key);

            if (count > config.getThreshold()) {
                return false;
            }
        } catch (Exception e) {
            // 限流器本身异常情况不能影响业务流程
            log.error(e,
                    LogMessage.create(this.getClass().getSimpleName(), "tryAcquire")
                            .fail()
                            .append("key", key)
                            .toString());
        }
        return true;
    }

    private void expireKey(String key) {
        try {
            if (ObjectUtil.defaultIfNull(redisTemplate.getExpire(key), -1L) == -1) {
                redisTemplate.expire(key, config.getExpirationMillis(), TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            log.error(e,
                    LogMessage.create(this.getClass().getSimpleName(), "expire")
                            .fail()
                            .append("key", key)
                            .toString());
        }
    }

    @Override
    public long countTryAcquireTimes(String key) {
        try {
            return Convert.toLong(redisTemplate.opsForValue().get(key), 0L);
        } catch (Exception e) {
            log.error(e, LogMessage.create(this.getClass().getSimpleName(), "countTryAcquireTimes")
                    .fail()
                    .append("key", key)
                    .toString());

            return 0;
        }
    }

    @Override
    public boolean canAcquire(String key) {
        return countTryAcquireTimes(key) < config.getThreshold();
    }

    @Override
    public RateLimiterConfig config() {
        return config;
    }

    public static class Factory implements RateLimiterFactory {

        @Getter
        private final RedisTemplate<String, String> redisTemplate;

        public Factory(RedisTemplate<String, ?> redisTemplate) {
            // 需要确保key和value为string序列化模式，否则反序列化时可能异常
            if (redisTemplate.getValueSerializer() instanceof StringRedisSerializer) {
                //noinspection unchecked
                this.redisTemplate = (RedisTemplate<String, String>) redisTemplate;
            } else {
                this.redisTemplate = new StringRedisTemplate(
                        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                );
            }
        }

        @Override
        public RateLimiter create(RateLimiterConfig config) {
            return new RedisCountRateLimiter(config, redisTemplate);
        }
    }
}