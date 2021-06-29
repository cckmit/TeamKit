package org.team4u.template.infrastructure;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateFunctionService;
import org.team4u.template.infrastructure.function.StringTemplateFunction;

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
}