package org.team4u.template.infrastructure;

import org.team4u.template.TemplateEngine;

public class JsTemplateEngineTest extends AbstractTemplateEngineTest {
    @Override
    protected Class<? extends TemplateEngine> templateEngineClass() {
        return JsTemplateEngine.class;
    }

    protected String template() {
        return "${a}-#s.md5u('1')";
    }
}