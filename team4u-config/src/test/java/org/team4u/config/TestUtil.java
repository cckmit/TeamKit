package org.team4u.config;

import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigId;

public class TestUtil {

    public static final String TEST_ID = "TEST1";

    public static final String TEST_ID2 = "TEST2";

    public static final String TEST_ID3 = "TEST3";

    public static SimpleConfig c(String key, String value) {
        return c(TEST_ID, key, value);
    }

    public static SimpleConfig c(String type, String key, String value) {
        return new SimpleConfig(
                new SimpleConfigId(type, key),
                value,
                null,
                0,
                true,
                null,
                null
        );
    }

    public static void checkConfigByDb(SimpleConfig config) {
        Assert.assertEquals(1, config.getSequenceNo());
        Assert.assertTrue(config.getEnabled());
        Assert.assertEquals(TestUtil.TEST_ID, config.getConfigId().getConfigKey());
        Assert.assertEquals(TestUtil.TEST_ID, config.getConfigId().getConfigType());
        Assert.assertEquals(TestUtil.TEST_ID, config.getConfigValue());
        Assert.assertEquals(TestUtil.TEST_ID, config.getCreatedBy());
        Assert.assertEquals(TestUtil.TEST_ID, config.getUpdatedBy());
        Assert.assertEquals(TestUtil.TEST_ID, config.getDescription());
        Assert.assertEquals("2020-08-21 00:00:00", DateUtil.formatDateTime(config.getCreateTime()));
        Assert.assertEquals("2020-08-21 00:00:00", DateUtil.formatDateTime(config.getUpdateTime()));
    }

    public static String[] ddlResourcePaths() {
        return new String[]{"sql/system_config.sql", "sql/system_config_data.sql"};
    }
}
