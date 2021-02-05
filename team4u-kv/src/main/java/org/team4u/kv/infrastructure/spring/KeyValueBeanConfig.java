package org.team4u.kv.infrastructure.spring;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.team4u.kv.KeyValueCleaner;
import org.team4u.kv.KeyValueRepository;
import org.team4u.kv.KeyValueService;
import org.team4u.kv.SimpleLockService;
import org.team4u.kv.infrastructure.repository.db.DbKeyValueMapper;
import org.team4u.kv.infrastructure.repository.db.DbKeyValueRepository;
import org.team4u.kv.infrastructure.repository.db.TableIdHandler;
import org.team4u.kv.infrastructure.resource.SimpleStoreResourceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 键值spring bean配置类
 *
 * @author jay.wu
 */
@Configuration
@ComponentScan("org.team4u.kv.infrastructure.repository.db")
public class KeyValueBeanConfig {

    @Bean
    public KeyValueCleaner keyValueCleaner(KeyValueCleaner.Config config,
                                           KeyValueRepository keyValueRepository) {
        SimpleLockService lock = new SimpleLockService(
                "KVC_LOCK",
                new KeyValueService(keyValueRepository, null)
        );

        KeyValueCleaner cleaner = new KeyValueCleaner(
                config,
                lock
        );
        cleaner.start();

        return cleaner;
    }

    @Bean
    public KeyValueRepository keyValueRepository(DbKeyValueMapper mapper, SimpleStoreResourceService.Config config) {
        return new DbKeyValueRepository(mapper, new SimpleStoreResourceService(
                config
        ));
    }

    @Bean
    public KeyValueService keyValueService(KeyValueRepository keyValueRepository, KeyValueCleaner keyValueCleaner) {
        return new KeyValueService(keyValueRepository, keyValueCleaner);
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
        Map<String, TableNameHandler> map = new HashMap<>(tableIdHandlers.size());
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
}