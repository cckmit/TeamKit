package org.team4u.template.infrastructure;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReflectUtil;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.team4u.template.InterpretContext;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateFunction;
import org.team4u.template.TemplateFunctionService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 基于beetl的模板引擎
 *
 * @author jay.wu
 */
public class BeetlTemplateEngine implements TemplateEngine {

    private final TemplateFunctionService templateFunctionService;
    private GroupTemplate groupTemplate;

    public BeetlTemplateEngine(TemplateFunctionService templateFunctionService) {
        if (templateFunctionService == null) {
            templateFunctionService = new TemplateFunctionService(null);
        }

        this.templateFunctionService = templateFunctionService;
        initGroupTemplate();
    }

    @Override
    public String render(String template, Map<String, Object> bindings) {
        Dict newBindings = new Dict();
        if (bindings != null) {
            newBindings.putAll(bindings);
        }

        InterpretContext.current().valueStack().putAll(newBindings);
        Template t = groupTemplate.getTemplate(template);
        t.binding(newBindings);
        return t.render();
    }

    /**
     * 初始化beetl模板引擎
     */
    private void initGroupTemplate() {
        try {
            groupTemplate = new GroupTemplate(
                    new StringTemplateResourceLoader(),
                    Configuration.defaultConfiguration()
            );

            registerFunctions(groupTemplate);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    /**
     * 注册自定义函数
     */
    private void registerFunctions(GroupTemplate gt) {
        for (TemplateFunction idObject : templateFunctionService.policies()) {
            for (Method method : ReflectUtil.getMethods(idObject.getClass())) {
                gt.registerFunction(
                        idObject.id() + "." + method.getName(),
                        (paras, ctx) -> ReflectUtil.invoke(idObject, method, paras)
                );
            }
        }
    }
}