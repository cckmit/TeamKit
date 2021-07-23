package org.team4u.rl.application;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.rl.domain.RateLimitConfigRepository;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;
import org.team4u.rl.domain.RateLimiterFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流器应用服务
 *
 * @author jay.wu
 */
public class RateLimiterAppService {

    private final Log log = Log.get();

    private final RateLimiters limiters;

    public RateLimiterAppService(RateLimiterFactory rateLimiterFactory,
                                 RateLimitConfigRepository configRepository) {
        limiters = new RateLimiters(rateLimiterFactory, configRepository);
    }

    /**
     * 根据类型尝试是否允许获取
     *
     * @param type 类型
     * @param key  键值
     * @return true为可以获取，false为拒绝获取
     */
    public boolean tryAcquire(String type, String key) {
        RateLimiter rateLimiter = limiters.limiterOf(type);

        LogMessage lm = logMessageOf(rateLimiter, "tryAcquire", type, key);

        // 不存在限流统计器不做拦截
        if (rateLimiter == null) {
            if (log.isDebugEnabled()) {
                log.debug(lm.fail("rateLimiter not exist").append("result", true).toString());
            }
            return true;
        }

        boolean result = rateLimiter.tryAcquire(key);

        if (log.isDebugEnabled()) {
            log.debug(lm.success().append("result", result).toString());
        }

        return result;
    }

    /**
     * 统计尝试获取次数
     *
     * @param type 类型
     * @param key  键值
     * @return 成功获取次数
     */
    public long countTryAcquireTimes(String type, String key) {
        RateLimiter rateLimiter = limiters.limiterOf(type);

        LogMessage lm = logMessageOf(rateLimiter, "countAcquired", type, key);

        // 不存在限流统计器不做拦截
        if (rateLimiter == null) {
            if (log.isDebugEnabled()) {
                log.debug(lm.fail("rateLimiter not exist").append("result", 0).toString());
            }
            return 0;
        }

        long result = rateLimiter.countTryAcquireTimes(key);

        if (log.isDebugEnabled()) {
            log.debug(lm.success().append("result", result).toString());
        }

        return result;
    }

    /**
     * 是否可以获取
     *
     * @param type 类型
     * @param key  键值
     * @return 是否可以获取
     */
    public boolean canAcquire(String type, String key) {
        RateLimiter rateLimiter = limiters.limiterOf(type);
        LogMessage lm = logMessageOf(rateLimiter, "canAcquire", type, key);

        // 不存在限流统计器不做拦截
        if (rateLimiter == null) {
            if (log.isDebugEnabled()) {
                log.debug(lm.fail("rateLimiter not exist").append("result", true).toString());
            }
            return true;
        }

        boolean result = rateLimiter.canAcquire(key);

        if (log.isDebugEnabled()) {
            log.debug(lm.success().append("result", result).toString());
        }

        return result;
    }

    /**
     * 根据类型获取限流器
     */
    public RateLimiter rateLimiterOf(String type) {
        return limiters.limiterOf(type);
    }

    private LogMessage logMessageOf(RateLimiter rateLimiter,
                                    String eventName,
                                    String type,
                                    String key) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), eventName);

        if (rateLimiter != null) {
            lm.append("limiter", rateLimiter.getClass().getSimpleName());
        }

        return lm.append("type", type).append("key", key);
    }

    public static class RateLimiters {
        private final Log log = LogFactory.get();

        private final RateLimiterFactory rateLimiterFactory;
        private final RateLimitConfigRepository rateLimitConfigRepository;

        private final Map<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

        public RateLimiters(RateLimiterFactory rateLimiterFactory,
                            RateLimitConfigRepository rateLimitConfigRepository) {
            this.rateLimiterFactory = rateLimiterFactory;
            this.rateLimitConfigRepository = rateLimitConfigRepository;
        }

        public RateLimiter limiterOf(String limiterType) {
            RateLimiterConfig config = rateLimitConfigRepository.configOf(limiterType);
            RateLimiter limiter = rateLimiters.get(limiterType);

            // 存在限流器，且配置未变化，直接返回
            if (limiter != null) {
                if (limiter.config().equals(config)) {
                    return limiter;
                }

                log.info(LogMessage.create(this.getClass().getSimpleName(), "limiterOf")
                        .success()
                        .append("limiter", limiterType)
                        .append("hasConfigChange", true)
                        .toString());
            }

            synchronized (this) {
                // 确保不会并发创建
                if (limiter == null) {
                    limiter = rateLimiters.get(limiterType);
                    if (limiter != null) {
                        return limiter;
                    }
                }

                return createRateLimiter(limiterType, config);
            }
        }

        private RateLimiter createRateLimiter(String limiterType, RateLimiterConfig config) {
            RateLimiter limiter = rateLimiterFactory.create(config);
            rateLimiters.put(limiterType, limiter);

            log.info(LogMessage.create(this.getClass().getSimpleName(), "createRateLimiter")
                    .success()
                    .append("limiter", limiterType)
                    .toString());

            return limiter;
        }
    }
}