package org.team4u.config.infrastructure.converter.bytebuddy;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.team4u.config.TestUtil;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.converter.AbstractConfigConverterTest;
import org.team4u.config.domain.repository.CacheableSimpleConfigRepository;
import org.team4u.config.domain.repository.MapSimpleConfigRepository;
import org.team4u.test.Benchmark;

public class ByteBuddyConfigConverterTest extends AbstractConfigConverterTest {

    @Override
    protected SimpleConfigConverter newSimpleConfigConverter() {
        return new ByteBuddyConfigConverter(repository);
    }

    @Override
    protected void additionalTypeCheck(TestConfig config) {
        // 配置项变动
        repository.allConfigs().getValue().get(0).setConfigValue("2");
        ThreadUtil.sleep(1000);
        Assert.assertEquals(2, config.getA());
        Assert.assertTrue(config.isF());
    }

    @Test
    @Ignore
    public void benchmark() {
        SimpleConfigRepository repository1 = new MapSimpleConfigRepository(
                Dict.create().set(TestUtil.TEST_ID + ".a", "1")
        );
        CacheableSimpleConfigRepository repository2 = new CacheableSimpleConfigRepository(
                new CacheableSimpleConfigRepository.Config().setMaxEffectiveSec(5),
                repository1
        );
        ByteBuddyConfigConverter converter = new ByteBuddyConfigConverter(repository2);
        TestConfig config = converter.to(
                TestConfig.class,
                TestUtil.TEST_ID
        );

        Benchmark benchmark = new Benchmark();
        benchmark.setPrintError(true);
        benchmark.start(5, () -> Assert.assertEquals(1, config.getA()));
    }
}