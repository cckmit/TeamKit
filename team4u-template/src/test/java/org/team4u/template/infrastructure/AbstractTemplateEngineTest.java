package org.team4u.template.infrastructure;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.SecureUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateFunction;
import org.team4u.template.TemplateFunctionService;

public abstract class AbstractTemplateEngineTest {

    @Test
    public void render() {
        TemplateEngine engine = ReflectUtil.newInstance(
                templateEngineClass(),
                new TemplateFunctionService(CollUtil.newArrayList(new StringTemplateFunction()))
        );

        Assert.assertEquals(
                "1-C4CA4238A0B923820DCC509A6F75849B",
                engine.render(template(), Dict.create().set("a", 1))
        );
    }

    protected String template() {
        return "${a}-${md5u('1')}";
    }

    protected abstract Class<? extends TemplateEngine> templateEngineClass();

    public static class StringTemplateFunction implements TemplateFunction {

        @Override
        public String id() {
            return "s";
        }

        /**
         * 大写md5值
         */
        public static String md5u(String value) {
            return SecureUtil.md5(value).toUpperCase();
        }

        /**
         * 小写md5值
         */
        public static String md5l(String value) {
            return md5u(value).toLowerCase();
        }
    }
}