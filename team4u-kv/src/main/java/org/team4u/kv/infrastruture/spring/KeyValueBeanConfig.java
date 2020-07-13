package org.team4u.kv.infrastruture.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.team4u.kv.KeyValueCleaner;
import org.team4u.kv.KeyValueRepository;
import org.team4u.kv.KeyValueService;
import org.team4u.kv.SimpleLockService;
import org.team4u.kv.infrastruture.repository.db.DbKeyValueMapper;
import org.team4u.kv.infrastruture.repository.db.DbKeyValueRepository;
import org.team4u.kv.infrastruture.resource.SimpleStoreResourceService;

/**
 * 键值spring bean配置类
 *
 * @author jay.wu
 */
@Configuration
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
}