package org.team4u.template;

import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 模板引擎服务
 *
 * @author jay.wu
 */
public class TemplateEngines extends PolicyRegistrar<String, TemplateEngine> {

    public TemplateEngines() {
        registerPoliciesByBeanProvidersAndEvent();
    }
}