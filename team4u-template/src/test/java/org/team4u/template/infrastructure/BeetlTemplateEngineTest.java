package org.team4u.template.infrastructure;

import org.team4u.template.TemplateEngine;

public class BeetlTemplateEngineTest extends AbstractTemplateEngineTest {

    @Override
    protected Class<? extends TemplateEngine> templateEngineClass() {
        return BeetlTemplateEngine.class;
    }
}