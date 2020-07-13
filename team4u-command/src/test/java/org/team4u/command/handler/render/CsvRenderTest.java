package org.team4u.command.handler.render;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.command.TestUtil;
import org.team4u.command.handler.HandlerAttributesKeys;
import org.team4u.command.handler.HandlerConfig;
import org.team4u.base.lang.EasyMap;

public class CsvRenderTest {

    @Test
    public void internalHandle() {
        CsvRender handler = new CsvRender(
                TestUtil.newTemplateEngine(),
                TestUtil.newInterceptorService()
        );


        EasyMap attrs = new EasyMap().set(
                HandlerAttributesKeys.TARGET,
                CollUtil.newArrayList(Dict.create().set("a", 1).set("b", 2))
        );

        Object result = handler.internalHandle(new HandlerConfig(), attrs);

        Assert.assertEquals("a,b\r\n" +
                "1,2\r\n", result.toString());
    }
}