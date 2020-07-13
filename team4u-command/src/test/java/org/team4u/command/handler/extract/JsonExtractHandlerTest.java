package org.team4u.command.handler.extract;

import cn.hutool.core.lang.Dict;
import org.team4u.command.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.lang.EasyMap;

public class JsonExtractHandlerTest {

    @Test
    public void handle() {
        JsonExtractHandler handler = new JsonExtractHandler(
                TestUtil.newTemplateEngine(),
                TestUtil.newInterceptorService()
        );

        EasyMap attrs = new EasyMap().set("source", Dict.create().set("x", 1));
        ExtractConfig config = new ExtractConfig().setTemplate("{'y':'x'}");
        config.setSourceKey("source");

        Object result = handler.internalHandle(config, attrs);

        Assert.assertEquals("{\"y\":1}", result.toString());
    }
}