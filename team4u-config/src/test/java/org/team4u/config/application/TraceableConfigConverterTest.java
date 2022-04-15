package org.team4u.config.application;

import org.junit.Assert;
import org.team4u.config.TestUtil;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.config.domain.repository.CacheableSimpleConfigRepository;

public class TraceableConfigConverterTest extends AbstractConfigConverterTest {

    private final MockSimpleConfigRepository mockRepository = new MockSimpleConfigRepository();
    private final CacheableSimpleConfigRepository cacheRepository = new CacheableSimpleConfigRepository(
            new CacheableSimpleConfigRepository.Config(),
            mockRepository
    );
    private final SimpleConfigConverter converter = new TraceableConfigConverter(cacheRepository);

    @Override
    protected SimpleConfigConverter converter() {
        return converter;
    }

    @Override
    protected MockSimpleConfigRepository mockRepository() {
        return mockRepository;
    }

    @Override
    protected void additionalTypeCheck(TestConfig config) {
        // 配置项变动
        SimpleConfigs configs = mockRepository.allConfigs();
        configs.getValue().get(0).setConfigValue("2");
        cacheRepository.refresh();

        Assert.assertEquals(2, config.getA());
        Assert.assertTrue(config.isF());
    }

    @Override
    protected void additionalValueCheck() {
        Demo g = converter().to(TestUtil.TEST_ID, "g", Demo.class);

        SimpleConfigs configs = mockRepository.allConfigs();
        configs.getValue().get(6).setConfigValue(
                "  {\n" +
                        "    \"a\": \"2\",\n" +
                        "    \"b\": \"2\",\n" +
                        "    \"c\": \"3\"\n" +
                        "  }\n"
        );
        cacheRepository.refresh();

        Assert.assertEquals(new Demo("2", "2"), g);
    }
}