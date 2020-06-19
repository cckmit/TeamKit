package org.team4u.kv;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.ds.simple.SimpleDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.team4u.kv.infrastruture.repository.db.DbKeyValueMapper;
import org.team4u.kv.infrastruture.repository.db.DbKeyValueRepository;
import org.team4u.kv.infrastruture.repository.db.DynamicTableNameParser;
import org.team4u.kv.infrastruture.resource.SimpleStoreResourceService;
import org.team4u.kv.resource.StoreResourceService;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;

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
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setSqlParserList(CollUtil.newArrayList(new DynamicTableNameParser()));
        return paginationInterceptor;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("org.team4u.kv");
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        configurer.setAnnotationClass(Repository.class);
        return configurer;
    }

    @Bean
    public KeyValueRepository keyValueRepository(DbKeyValueMapper mapper, StoreResourceService storeResourceService) {
        return new DbKeyValueRepository(mapper, storeResourceService);
    }

    @Bean
    public SimpleStoreResourceService storeResourceService() {
        return new SimpleStoreResourceService(new SimpleStoreResourceService.Config()
                .setResourceType("DB")
                .setResourceName("key_value")
                .setResourceCount(2)
                .setMaxResourceCount(4));
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}