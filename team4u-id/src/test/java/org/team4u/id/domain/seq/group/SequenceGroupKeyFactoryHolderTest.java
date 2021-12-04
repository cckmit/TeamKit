package org.team4u.id.domain.seq.group;

import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.id.domain.seq.SequenceConfig;

import java.util.Date;

public class SequenceGroupKeyFactoryHolderTest {

    private final SequenceGroupKeyFactoryHolder holder = new SequenceGroupKeyFactoryHolder();

    @Test
    public void nextWithDefaultConfig() {
        Assert.assertEquals(
                DateUtil.format(new Date(), "yyyyMMdd"),
                next(null)
        );
    }

    @Test
    public void nextWithDefaultCustom() {
        String key = next("{\n" +
                "  \"format\": \"yyyy-MM-dd\"\n" +
                "}");
        Assert.assertEquals(DateUtil.formatDate(new Date()), key);
    }

    private String next(String config) {
        SequenceConfig c = new SequenceConfig();
        c.setGroupFactoryId("DT");
        c.setGroupConfig(config);
        return holder.provide(new SequenceGroupKeyProvider.Context(c, null));
    }
}