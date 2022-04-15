package org.team4u.config.domain.converter;

import org.junit.Assert;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.config.domain.repository.CacheableSimpleConfigRepository;

public class TraceableConfigConverterTest extends AbstractConfigConverterTest {

    private final MockSimpleConfigRepository repository = new MockSimpleConfigRepository();
    private final CacheableSimpleConfigRepository cacheRepository = new CacheableSimpleConfigRepository(
            new CacheableSimpleConfigRepository.Config(),
            repository
    );
    private final SimpleConfigConverter converter = new TraceableConfigConverter(cacheRepository);

    @Override
    protected SimpleConfigConverter converter() {
        return converter;
    }

    @Override
    protected void additionalTypeCheck(TestConfig config) {
        // 配置项变动
        SimpleConfigs configs = repository.allConfigs();
        configs.getValue().get(0).setConfigValue("2");
        cacheRepository.refresh();

        Assert.assertEquals(2, config.getA());
        Assert.assertTrue(config.isF());
    }
}