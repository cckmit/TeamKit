package org.team4u.workflow.infrastructure;

import cn.hutool.db.ds.simple.SimpleDataSource;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.infrastructure.persistence.memory.LogOnlyEventStore;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.infrastructure.persistence.definition.JsonProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.instance.MybatisProcessInstanceRepository;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessAssigneeMapper;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessInstanceDetailMapper;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessInstanceMapper;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class BeanConfig {

    @Bean
    public DataSource dataSource() {
        return new SimpleDataSource("jdbc:h2:mem:testdb", "root", "");
    }

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource,
                                                          List<InnerInterceptor> interceptors) {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.setInterceptors(interceptors);
        bean.setPlugins(mybatisPlusInterceptor);
        return bean;
    }

    @Bean
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("org.team4u");
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        configurer.setAnnotationClass(Repository.class);
        return configurer;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public EventStore eventStore() {
        return new LogOnlyEventStore();
    }

    @Bean
    public ProcessInstanceRepository processInstanceRepository(EventStore eventStore,
                                                               ProcessInstanceMapper instanceMapper,
                                                               ProcessAssigneeMapper assigneeMapper,
                                                               ProcessInstanceDetailMapper instanceDetailMapper) {
        return new MybatisProcessInstanceRepository(
                eventStore,
                instanceMapper,
                assigneeMapper,
                instanceDetailMapper,
                new JsonProcessDefinitionRepository(new LocalJsonConfigService())
        );
    }
}