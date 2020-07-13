package org.team4u.command;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import org.team4u.command.config.LocalJsonCommandConfigRepository;
import org.team4u.command.handler.AbstractHandler;
import org.team4u.command.handler.HandlerAttributesKeys;
import org.team4u.command.handler.code.CodeMappingHandler;
import org.team4u.command.handler.extract.JsonExtractHandler;
import org.team4u.command.handler.remote.FakeHttpHandler;
import org.team4u.command.handler.remote.SimpleHttpResponse;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.lang.EasyMap;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateFunction;
import org.team4u.template.TemplateFunctionService;
import org.team4u.template.infrastructure.BeetlTemplateEngine;
import org.team4u.template.infrastructure.function.StringTemplateFunction;

import static org.team4u.command.TestUtil.newInterceptorService;

/**
 * @author Jay Wu
 */
public class HandlerServiceTest {

    @Test
    public void invoke() {
        HandlerService handlerService = new HandlerService(
                CollUtil.newArrayList(
                        new FakeHandler("handler1", TestUtil.newInterceptorService(), new StringTemplateFunction()),
                        new FakeHandler("handler2", TestUtil.newInterceptorService())
                ),
                new LocalJsonCommandConfigRepository()
        );

        EasyMap attributes = handlerService.invoke("simple.template.json", new EasyMap());
        Assert.assertEquals("1", attributes.getStr("handler1_x"));
        Assert.assertEquals("1", attributes.getStr("handler2_x"));
        Assert.assertEquals("1", attributes.getStr("handler2_y"));
        Assert.assertEquals("C4CA4238A0B923820DCC509A6F75849B", attributes.getStr("handler1_y"));
    }

    @Test
    public void channel() {
        FakeTraceInterceptor interceptor = new FakeTraceInterceptor();
        HandlerInterceptorService interceptorService = newInterceptorService(interceptor);
        TemplateEngine templateFunction = TestUtil.newTemplateEngine();
        HandlerService handlerService = new HandlerService(
                CollUtil.newArrayList(
                        new FakeHttpHandler(interceptorService)
                                .setSimpleHttpResponse(new SimpleHttpResponse(HttpStatus.HTTP_OK, "{'x':'00'}")),
                        new JsonExtractHandler(templateFunction, interceptorService),
                        new CodeMappingHandler(templateFunction, interceptorService)
                ),
                new LocalJsonCommandConfigRepository()
        );
        EasyMap attributes = handlerService.invoke(
                "channel.template.json",
                new EasyMap().set("req", Dict.create().set("name", "1"))
        );
        Response result = attributes.getProperty(HandlerAttributesKeys.TARGET, Response.class);

        Assert.assertEquals("0000", result.getStandardCode());
        Assert.assertEquals("00", result.getCode());
        Assert.assertTrue(interceptor.isHasInserted());
        Assert.assertTrue(interceptor.isHasUpdated());
    }

    private static class FakeHandler extends AbstractHandler {

        private final String id;

        public FakeHandler(String id,
                           HandlerInterceptorService handlerInterceptorService,
                           TemplateFunction... functions) {
            super(new BeetlTemplateEngine(new TemplateFunctionService(CollUtil.toList(functions))), handlerInterceptorService);
            this.id = id;
        }

        @Override
        protected void handle(EasyMap config, EasyMap attributes) {
            System.out.println(config);
            attributes.set(id + "_x", 1);
            attributes.set(id + "_y", config.getStr("key1"));
        }

        @Override
        public String id() {
            return id;
        }
    }

    public static class Response {
        private String code;
        private String subCode;
        private String standardCode;

        public String getCode() {
            return code;
        }

        public Response setCode(String code) {
            this.code = code;
            return this;
        }

        public String getSubCode() {
            return subCode;
        }

        public Response setSubCode(String subCode) {
            this.subCode = subCode;
            return this;
        }

        public String getStandardCode() {
            return standardCode;
        }

        public Response setStandardCode(String standardCode) {
            this.standardCode = standardCode;
            return this;
        }
    }
}