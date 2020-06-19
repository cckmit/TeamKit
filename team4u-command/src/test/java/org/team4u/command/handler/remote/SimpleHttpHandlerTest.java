package org.team4u.command.handler.remote;

import cn.hutool.http.HttpStatus;
import cn.hutool.http.Method;
import org.team4u.command.TestUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleHttpHandlerTest {

    @Test
    @Ignore
    public void handle() {
        SimpleHttpHandler httpHandler = new SimpleHttpHandler(
                TestUtil.newTemplateEngine(),
                TestUtil.newInterceptorService()
        );

        SimpleHttpResponse resp = httpHandler.internalHandle(
                new HttpConfig()
                        .setUrl("http://baidu.com")
                        .setMethod(Method.GET.name()),
                null
        );

        Assert.assertNotNull(resp);
        Assert.assertEquals(HttpStatus.HTTP_MOVED_TEMP, resp.getStatus());
        Assert.assertTrue(resp.getBody().length() > 0);
    }
}