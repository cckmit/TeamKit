package org.team4u.rl.infrastructure.limiter;

import cn.hutool.core.convert.Convert;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.team4u.base.log.LogMessage;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的计数限流器
 *
 * @author jay.wu
 */
public class RedisCountRateLimiter implements RateLimiter {

    private final Log log = LogFactory.get();

    private final RateLimiterConfig.Rule config;
    private final RedisTemplate<String, String> redisTemplate;

    public RedisCountRateLimiter(RateLimiterConfig.Rule config, RedisTemplate<String, String> redisTemplate) {
        this.config = config;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean tryAcquire(String key) {
        try {
            long count = redisTemplate.opsForValue().increment(key, 1);

            if (count > config.getThreshold()) {
                return false;
            }
        } catch (Exception e) {
            // 限流器本身异常情况不能影响业务流程，返回true
            log.error(e,
                    LogMessage.create(this.getClass().getSimpleName(), "tryAcquire")
                            .fail()
                            .append("key", key)
                            .toString());
        } finally {
            try {
                //　防止异常情况没有设置过期时间
                if (redisTemplate.getExpire(key) == -1) {
                    redisTemplate.expire(key, config.getExpirationMillis(), TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                // Ignore error
            }
        }

        return true;
    }

    @Override
    public long tryAcquiredCount(String key) {
        return Convert.toLong(redisTemplate.opsForValue().get(key), 0L);
    }
}