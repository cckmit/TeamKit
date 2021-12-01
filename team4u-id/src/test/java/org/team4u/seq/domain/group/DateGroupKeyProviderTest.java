package org.team4u.seq.domain.group;

import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.id.domain.seq.group.DateGroupKeyProvider;

import java.util.Date;

public class DateGroupKeyProviderTest {

    @Test
    public void provide() {
        DateGroupKeyProvider provider = new DateGroupKeyProvider(new DateGroupKeyProvider.Config()) {
            @Override
            protected Date now() {
                return DateUtil.parseDate("2021-12-01");
            }
        };

        Assert.assertEquals("20211201", provider.provide(null));
    }
}