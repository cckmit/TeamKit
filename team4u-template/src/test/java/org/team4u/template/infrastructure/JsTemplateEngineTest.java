package org.team4u.template.infrastructure;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateEngines;

public class JsTemplateEngineTest extends AbstractTemplateEngineTest {
    @Override
    protected Class<? extends TemplateEngine> templateEngineClass() {
        return JsTemplateEngine.class;
    }

    @Test
    public void test() {
        TemplateEngines engines = new TemplateEngines();
        TemplateEngine js = engines.availablePolicyOf("JS");
        boolean x = Convert.toBool(js.render("${a == 1}", Dict.create().set("a", 1)));
        Assert.assertTrue(x);

        x = Convert.toBool(js.render("${a == 1}", Dict.create().set("a", 2)));
        Assert.assertFalse(x);
    }

    protected String template() {
        return "${a}-#s.md5u('1')";
    }
}