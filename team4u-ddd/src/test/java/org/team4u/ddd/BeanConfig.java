package org.team4u.ddd;

import cn.hutool.db.ds.simple.SimpleDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.infrastructure.persistence.memory.LogOnlyEventStore;
import org.team4u.ddd.infrastructure.persistence.mybatis.EventStoreMapper;
import org.team4u.ddd.infrastructure.persistence.mybatis.MybatisEventStore;
import org.team4u.ddd.infrastructure.persistence.mybatis.MybatisTimeConstrainedProcessTrackerRepository;
import org.team4u.ddd.infrastructure.persistence.mybatis.TimeConstrainedProcessTrackerMapper;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerRepository;
import org.team4u.ddd.process.strategy.FakeRetryStrategyRepository;

import javax.sql.DataSource;

@Configuration
public class BeanConfig {

    @Bean
    public DataSource dataSource() {
        return new SimpleDataSource("jdbc:h2:mem:testdb", "root", "");
    }

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, Interceptor[] interceptors) {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPlugins(interceptors);
        return bean;
    }

    @Bean
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("org.team4u.ddd");
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        configurer.setAnnotationClass(Repository.class);
        return configurer;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public EventStore eventStore(EventStoreMapper mapper) {
        return new MybatisEventStore(mapper, FakeLongIdentityFactory.getInstance());
    }

    @Bean
    public TimeConstrainedProcessTrackerRepository timeConstrainedProcessTrackerRepository(
            TimeConstrainedProcessTrackerMapper mapper) {
        return new MybatisTimeConstrainedProcessTrackerRepository(
                new LogOnlyEventStore(),
                mapper,
                new FakeRetryStrategyRepository());
    }
}