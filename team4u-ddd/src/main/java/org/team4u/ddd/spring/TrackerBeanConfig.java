package org.team4u.ddd.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.team4u.base.config.ConfigService;
import org.team4u.base.lang.FirstAvailableObjectProxy;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.infrastructure.persistence.mybatis.MybatisTimeConstrainedProcessTrackerRepository;
import org.team4u.ddd.infrastructure.persistence.mybatis.TimeConstrainedProcessTrackerMapper;
import org.team4u.ddd.infrastructure.process.strategy.ConfigRetryStrategyRepository;
import org.team4u.ddd.process.LocalProcessTimedOutInformer;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;
import org.team4u.ddd.process.strategy.RetryStrategyFactoryService;
import org.team4u.ddd.process.strategy.RetryStrategyRepository;
import org.team4u.kv.KeyValueService;
import org.team4u.kv.SimpleLockService;

import java.util.List;

/**
 * 跟踪器spring bean配置类
 *
 * @author jay.wu
 */
@Configuration
public class TrackerBeanConfig {

    @Bean
    @Order
    public ConfigRetryStrategyRepository configRetryStrategyRepository(ConfigService configService) {
        return new ConfigRetryStrategyRepository(configService);
    }

    @Bean
    public TimeConstrainedProcessTrackerAppService timeConstrainedProcessTrackerAppService(
            TimeConstrainedProcessTrackerMapper mapper,
            List<RetryStrategyRepository> retryStrategyRepositories,
            EventStore eventStore) {
        RetryStrategyRepository retryStrategyRepository = FirstAvailableObjectProxy.newProxyInstance(
                RetryStrategyRepository.class,
                retryStrategyRepositories,
                false
        );
        return new TimeConstrainedProcessTrackerAppService(
                new MybatisTimeConstrainedProcessTrackerRepository(eventStore, mapper, retryStrategyRepository),
                retryStrategyRepository
        );
    }

    @Bean
    public LocalProcessTimedOutInformer localProcessTimedOutInformer(
            LocalProcessTimedOutInformer.Config config,
            KeyValueService keyValueService,
            TimeConstrainedProcessTrackerAppService trackerAppService) {
        LocalProcessTimedOutInformer informer = new LocalProcessTimedOutInformer(
                config,
                new SimpleLockService("tracker", keyValueService),
                trackerAppService
        );

        informer.start();

        return informer;
    }

    @Bean
    public RetryStrategyFactoryService retryStrategyFactoryService() {
        return new RetryStrategyFactoryService();
    }
}