package org.team4u.sql.infrastructure.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;

import static org.team4u.sql.infrastructure.config.SpringResourceSqlConfigService.Config;

public class SpringResourceSqlConfigServiceTest {

    private final SpringResourceSqlConfigService s = new SpringResourceSqlConfigService(new Config());

    @Test
    public void demo1() {
        Assert.assertEquals("SELECT *\nFROM client;", s.get("demo1"));
    }

    @Test
    public void watch() {
        try {
            Assert.assertNull(s.get("demo2"));
            ThreadUtil.safeSleep(1000);
            FileUtil.writeUtf8String("/* demo2 */\nSELECT * FROM client;", "sqls/change.sql");
            ThreadUtil.safeSleep(1000);
            Assert.assertEquals("SELECT * FROM client;", s.get("demo2"));
        } finally {
            FileUtil.writeUtf8String("", "sqls/change.sql");
        }
    }
}