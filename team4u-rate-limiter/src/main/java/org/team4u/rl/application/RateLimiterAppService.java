package org.team4u.rl.application;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.log.LogMessage;
import org.team4u.rl.domain.RateLimitConfigRepository;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;
import org.team4u.rl.domain.RateLimiterFactory;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 限流器应用服务
 *
 * @author jay.wu
 */
public class RateLimiterAppService {

    private final Log log = Log.get();

    private final LimitersRefresher limiterRefresher;

    public RateLimiterAppService(LimitersRefresher.Config config,
                                 RateLimiterFactory rateLimiterFactory,
                                 RateLimitConfigRepository configRepository) {
        limiterRefresher = new LimitersRefresher(config, rateLimiterFactory, configRepository);
        limiterRefresher.start();
    }

    /**
     * 根据类型尝试是否允许获取
     *
     * @param type 类型
     * @param key  键值
     * @return true为可以获取，false为拒绝获取
     */
    public boolean tryAcquire(String type, String key) {
        RateLimiter rateLimiter = limiterRefresher.limiterOf(type);

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
        RateLimiter rateLimiter = limiterRefresher.limiterOf(type);

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
        RateLimiter rateLimiter = limiterRefresher.limiterOf(type);
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

    public static class LimitersRefresher extends LongTimeThread {
        private final Log log = LogFactory.get();

        private final Config config;

        private final RateLimiterFactory rateLimiterFactory;
        private final RateLimitConfigRepository rateLimitConfigRepository;

        private RateLimiterConfig rateLimiterConfig;
        private Map<String, RateLimiter> rateLimiters = Collections.emptyMap();

        public LimitersRefresher(Config config,
                                 RateLimiterFactory rateLimiterFactory,
                                 RateLimitConfigRepository rateLimitConfigRepository) {
            this.config = config;
            this.rateLimiterFactory = rateLimiterFactory;
            this.rateLimitConfigRepository = rateLimitConfigRepository;
        }

        @Override
        public synchronized void start() {
            onRun();
            super.start();
        }

        public RateLimiter limiterOf(String key) {
            return rateLimiters.get(key);
        }

        @Override
        protected void onRun() {
            RateLimiterConfig rateLimiterConfig = rateLimitConfigRepository.configOf(config.getConfigId());

            // 判断规则集合是否有变动，若有则重新构建
            if (ObjectUtil.equals(this.rateLimiterConfig, rateLimiterConfig)) {
                return;
            }

            this.rateLimiterConfig = rateLimiterConfig;
            initRateLimitersWithRules();
        }

        private boolean hasChanged() {
            return ObjectUtil.equals(this.rateLimiterConfig, rateLimiterConfig);
        }

        private void initRateLimitersWithRules() {
            rateLimiters = rateLimiterConfig.getRules()
                    .stream()
                    .collect(Collectors.toMap(
                            RateLimiterConfig.Rule::getType,
                            rateLimiterFactory::create
                    ));

            log.info(LogMessage.create(this.getClass().getSimpleName(), "initRateLimitersWithRules")
                    .success()
                    .append("rateLimiters", rateLimiters.size())
                    .toString());
        }

        @Override
        protected Number runIntervalMillis() {
            return config.getRefreshConfigIntervalMillis();
        }

        /**
         * 配置类
         */
        public static class Config {
            private int refreshConfigIntervalMillis = 5000;
            /**
             * 配置标识
             */
            private String configId;

            public int getRefreshConfigIntervalMillis() {
                return refreshConfigIntervalMillis;
            }

            public Config setRefreshConfigIntervalMillis(int refreshConfigIntervalMillis) {
                this.refreshConfigIntervalMillis = refreshConfigIntervalMillis;
                return this;
            }

            public String getConfigId() {
                return configId;
            }

            public Config setConfigId(String configId) {
                this.configId = configId;
                return this;
            }
        }
    }
}