package org.team4u.config.infrastructure.persistence;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigId;

import java.util.List;

public class PropSimpleConfigRepositoryTest {

    @Test
    public void allConfigs() {
        PropSimpleConfigRepository repository = new PropSimpleConfigRepository("test.properties");
        List<SimpleConfig> configs = repository.allConfigs();
        Assert.assertEquals(2, configs.size());
        checkConfig(configs.get(0), "test2", "b", "2");
        checkConfig(configs.get(1), "test1", "a", "1");

    }

    private void checkConfig(SimpleConfig config,
                             String type,
                             String key,
                             String value) {
        Assert.assertEquals(new SimpleConfigId(type, key), config.getConfigId());
        Assert.assertEquals(value, config.getConfigValue());
        Assert.assertTrue(config.getEnabled());
    }
}