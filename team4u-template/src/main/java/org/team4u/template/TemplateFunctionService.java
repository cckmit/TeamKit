package org.team4u.template;


import org.team4u.base.registrar.PolicyRegistrar;

import java.util.List;

/**
 * 自定义模板函数服务
 *
 * @author jay.wu
 */
public class TemplateFunctionService extends PolicyRegistrar<String, TemplateFunction> {

    public TemplateFunctionService() {
        registerByBeanProvidersAndEvent();
    }

    public TemplateFunctionService(List<TemplateFunction> functions) {
        super(functions);
    }
}