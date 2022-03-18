package org.team4u.template.infrastructure;

import cn.hutool.crypto.SecureUtil;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateFunction;

public class JetbrickTemplateEngineTest extends AbstractTemplateEngineTest {

    @Override
    protected Class<? extends TemplateEngine> templateEngineClass() {
        return JetbrickTemplateEngine.class;
    }

    @Override
    protected TemplateFunction function() {
        return new StringTemplateFunction();
    }

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