package org.team4u.config;

import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigId;

public class TestUtil {

    public static final String TEST_ID = "TEST1";

    public static final String TEST_ID2 = "TEST2";

    public static final String TEST_ID3 = "TEST3";

    public static SimpleConfig c(String key, String value) {
        return new SimpleConfig(
                new SimpleConfigId(TestUtil.TEST_ID, key),
                value,
                null,
                0,
                true,
                null,
                null
        );
    }
}
