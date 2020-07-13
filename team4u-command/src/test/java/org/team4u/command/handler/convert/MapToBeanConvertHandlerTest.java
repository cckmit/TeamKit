package org.team4u.command.handler.convert;

import cn.hutool.core.lang.Dict;
import org.team4u.command.handler.HandlerAttributesKeys;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.lang.EasyMap;

import static org.team4u.command.TestUtil.newInterceptorService;
import static org.team4u.command.TestUtil.newTemplateEngine;

public class MapToBeanConvertHandlerTest {

    @Test
    public void internalHandle() {
        MapToBeanConvertHandler handler = new MapToBeanConvertHandler(
                newTemplateEngine(),
                newInterceptorService()
        );

        Demo result = (Demo) handler.internalHandle(
                new ConvertConfig().setTargetType(Demo.class.getName()),
                new EasyMap().set(HandlerAttributesKeys.TARGET, Dict.create().set("name", "1"))
        );

        Assert.assertEquals("1", result.getName());
    }

    public static class Demo {
        private String name;

        public String getName() {
            return name;
        }

        public Demo setName(String name) {
            this.name = name;
            return this;
        }
    }
}