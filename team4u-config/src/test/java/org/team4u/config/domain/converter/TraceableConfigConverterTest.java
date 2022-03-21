package org.team4u.config.domain.converter;

import org.junit.Assert;
import org.team4u.config.domain.SimpleConfigComparator;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigs;

public class TraceableConfigConverterTest extends AbstractConfigConverterTest {

    @Override
    protected SimpleConfigConverter newSimpleConfigConverter() {
        return new TraceableConfigConverter(repository);
    }

    @Override
    protected void additionalTypeCheck(TestConfig config) {
        // 配置项变动
        SimpleConfigs newConfigs = repository.allConfigs().copy();
        newConfigs.getValue().get(0).setConfigValue("2");
        new SimpleConfigComparator().compareAndPublishEvents(repository.allConfigs().getValue(), newConfigs.getValue());

        Assert.assertEquals(2, config.getA());
        Assert.assertTrue(config.isF());
    }
}