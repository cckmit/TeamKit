package org.team4u.id.domain.seq.group;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DateTimeGroupKeyProviderTest {

    @Test
    public void provide() {
        DateTimeGroupKeyProvider provider = new DateTimeGroupKeyProvider(new DateTimeGroupKeyProvider.Config()) {
            @Override
            protected Date now() {
                return DateUtil.parseDate("2021-12-01");
            }
        };

        Assert.assertEquals("20211201", provider.provide(null));
    }

    @Test
    public void create() {
        DateTimeGroupKeyProvider.Factory factory = new DateTimeGroupKeyProvider.Factory();
        DateTimeGroupKeyProvider p = (DateTimeGroupKeyProvider) factory.create(
                FileUtil.readUtf8String("date_group_key_config.json")
        );

        Assert.assertEquals("yyyy", p.getConfig().getFormat());
    }
}