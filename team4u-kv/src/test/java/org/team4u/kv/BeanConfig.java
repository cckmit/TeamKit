package org.team4u.kv;

import cn.hutool.db.ds.simple.SimpleDataSource;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.team4u.kv.infrastructure.repository.db.DbKeyValueMapper;
import org.team4u.kv.infrastructure.repository.db.DbKeyValueRepository;
import org.team4u.kv.infrastructure.repository.db.TableIdHandler;
import org.team4u.kv.infrastructure.resource.SimpleStoreResourceService;
import org.team4u.kv.resource.StoreResourceService;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan("org.team4u.kv.infrastructure.repository.db")
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
    public MybatisPlusInterceptor mybatisPlusInterceptor(List<InnerInterceptor> innerInterceptorList) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        for (InnerInterceptor innerInterceptor : innerInterceptorList) {
            interceptor.addInnerInterceptor(innerInterceptor);
        }
        return interceptor;
    }

    @Bean
    public DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor(List<TableIdHandler> tableIdHandlers) {
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        Map<String, TableNameHandler> map = new HashMap<>();
        for (TableIdHandler tableIdHandler : tableIdHandlers) {
            map.put(tableIdHandler.tableName(), tableIdHandler);
        }
        dynamicTableNameInnerInterceptor.setTableNameHandlerMap(map);
        return dynamicTableNameInnerInterceptor;
    }

    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        return new PaginationInnerInterceptor();
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