package org.team4u.kv;

import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.team4u.kv.infrastructure.repository.db.DbKeyValueMapper;
import org.team4u.kv.infrastructure.repository.db.DbKeyValueRepository;
import org.team4u.kv.infrastructure.repository.db.TableIdHandler;
import org.team4u.kv.infrastructure.resource.SimpleStoreResourceService;
import org.team4u.kv.resource.StoreResourceService;
import org.team4u.test.spring.DbTestBeanConfig;

@Configuration
@Import(DbTestBeanConfig.class)
@ComponentScan("org.team4u.kv.infrastructure.repository.db")
public class BeanConfig {

    @Bean
    public DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor(TableIdHandler tableIdHandler) {
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler(tableIdHandler);
        return dynamicTableNameInnerInterceptor;
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
}