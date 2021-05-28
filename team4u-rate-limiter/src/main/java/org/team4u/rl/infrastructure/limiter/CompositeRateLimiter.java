package org.team4u.rl.infrastructure.limiter;

import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;
import org.team4u.rl.domain.RateLimiterFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组合限流应用服务
 *
 * @author jay.wu
 */
public class CompositeRateLimiter implements RateLimiter {

    private final List<RateLimiter> limiters;

    public CompositeRateLimiter(List<RateLimiter> limiters) {
        this.limiters = limiters;
    }

    /**
     * 根据类型尝试所有限流器是否均可访问
     *
     * @param key 键值
     * @return true为可以访问，false为拒绝访问
     */
    @Override
    public boolean tryAcquire(String key) {
        return limiters
                .stream()
                .allMatch(it -> it.tryAcquire(key));
    }

    /**
     * 统计所有限流器中最大的访问次数
     *
     * @param key 键值
     * @return 成功访问次数
     */
    @Override
    public long countAcquired(String key) {
        return limiters
                .stream()
                .map(it -> it.countAcquired(key))
                .max(Long::compareTo)
                .orElse(0L);
    }

    /**
     * 判断所有限流器是否均可访问
     *
     * @param key 键值
     * @return 是否可以访问
     */
    @Override
    public boolean canAcquire(String key) {
        return limiters
                .stream()
                .allMatch(it -> it.canAcquire(key));
    }

    public static class Factory implements RateLimiterFactory {

        private final RateLimiterFactory[] factories;

        public Factory(RateLimiterFactory... factories) {
            this.factories = factories;
        }

        @Override
        public RateLimiter create(RateLimiterConfig.Rule rule) {
            return new CompositeRateLimiter(Arrays.stream(factories)
                    .map(it -> it.create(rule))
                    .collect(Collectors.toList()));
        }
    }
}