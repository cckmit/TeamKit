package org.team4u.template.infrastructure;

import cn.hutool.core.lang.Dict;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.resolver.GlobalResolver;
import org.team4u.template.InterpretContext;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateFunction;
import org.team4u.template.TemplateFunctionService;

import java.io.StringWriter;
import java.util.Map;

/**
 * 基于jetbrick的模板引擎
 *
 * @author jay.wu
 */
public class JetbrickTemplateEngine implements TemplateEngine {

    private final JetEngine engine = JetEngine.create();
    private final TemplateFunctionService templateFunctionService;

    public JetbrickTemplateEngine(TemplateFunctionService templateFunctionService) {
        if (templateFunctionService == null) {
            templateFunctionService = new TemplateFunctionService(null);
        }

        this.templateFunctionService = templateFunctionService;
        registerFunctions();
    }

    @Override
    public String render(String template, Map<String, Object> bindings) {
        Dict newBindings = new Dict();
        if (bindings != null) {
            newBindings.putAll(bindings);
        }

        InterpretContext.current().valueStack().putAll(newBindings);
        JetTemplate jetTemplate = engine.createTemplate(template);
        StringWriter sw = new StringWriter();
        jetTemplate.render(newBindings, sw);
        return sw.toString();
    }


    /**
     * 注册自定义函数
     */
    private void registerFunctions() {
        GlobalResolver resolver = engine.getGlobalResolver();
        for (TemplateFunction idObject : templateFunctionService.idObjects()) {
            resolver.registerFunctions(idObject.getClass());
        }
    }
}