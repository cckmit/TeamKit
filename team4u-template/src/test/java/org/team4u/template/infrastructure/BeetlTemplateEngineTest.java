package org.team4u.template.infrastructure;

import org.team4u.template.TemplateEngine;

public class BeetlTemplateEngineTest extends AbstractTemplateEngineTest {

    @Override
    protected String template() {
        return "${a}-${s.md5u('1')}";
    }

    @Override
    protected Class<? extends TemplateEngine> templateEngineClass() {
        return BeetlTemplateEngine.class;
    }
}