package org.team4u.command.handler.extract;

import org.team4u.command.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.lang.EasyMap;

public class XmlExtractHandlerTest {

    @Test
    public void handle() {
        XmlExtractHandler handler = new XmlExtractHandler(
                TestUtil.newTemplateEngine(),
                TestUtil.newInterceptorService()
        );

        EasyMap attrs = new EasyMap().set("source", "<xml><x>1</x></xml>");
        ExtractConfig config = new ExtractConfig().setTemplate("{'y':'x'}");
        config.setSourceKey("source");
        Object result = handler.internalHandle(config, attrs);

        Assert.assertEquals("{\"y\":\"1\"}", result.toString());
    }
}