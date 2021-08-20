package org.team4u.template.infrastructure;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReUtil;
import cn.hutool.script.ScriptRuntimeException;
import cn.hutool.script.ScriptUtil;
import org.team4u.template.InterpretContext;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateFunction;
import org.team4u.template.TemplateFunctionService;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基于js的模板引擎
 *
 * @author jay.wu
 */
public class JsTemplateEngine implements TemplateEngine {

    private final Cache<String, CompiledScript> scripts = CacheUtil.newLRUCache(1000);
    private final TemplateFunctionService templateFunctionService;

    public JsTemplateEngine(TemplateFunctionService templateFunctionService) {
        if (templateFunctionService == null) {
            templateFunctionService = new TemplateFunctionService(null);
        }

        this.templateFunctionService = templateFunctionService;
    }

    @Override
    public String render(String template, Map<String, Object> bindings) {
        Dict newBindings = new Dict(customFunctions());
        if (bindings != null) {
            newBindings.putAll(bindings);
        }

        InterpretContext.current().valueStack().putAll(newBindings);

        template = renderVar(template, newBindings);
        template = renderFunction(template, newBindings);
        return template;
    }

    /**
     * 渲染自定义函数
     */
    private String renderFunction(String template, Dict bindings) {
        for (String placeholder : functionPlaceholders(template)) {
            String returnOfFunc = evalScript(placeholder, bindings);
            template = template.replace("#" + placeholder, returnOfFunc);
            InterpretContext.current().setRenderedContent(template);
        }

        return template;
    }

    /**
     * 函数占位符集合
     */
    private List<String> functionPlaceholders(String template) {
        return ReUtil.findAll("#(.+?\\(*?\\))", template, 1);
    }

    /**
     * 自定义函数集合
     */
    private Dict customFunctions() {
        Dict functions = Dict.create();
        for (TemplateFunction idObject : templateFunctionService.policies()) {
            functions.set(idObject.id(), idObject);
        }

        return functions;
    }

    /**
     * 渲染变量
     */
    private String renderVar(String template, Dict bindings) {
        for (String placeholder : varPlaceholders(template)) {
            String returnOfVar = evalScript(placeholder, bindings);
            template = template.replace("${" + placeholder + "}", returnOfVar);
            InterpretContext.current().setRenderedContent(template);
        }
        return template;
    }

    /**
     * 变量占位符集合
     */
    private Collection<String> varPlaceholders(String template) {
        // ${a.b}
        return ReUtil.findAll("\\$\\{(.+?)\\}", template, 1);
    }

    private String evalScript(String script, Dict bindings) {
        CompiledScript cs = scripts.get(script);

        synchronized (scripts) {
            if (scripts.get(script) == null) {
                cs = ScriptUtil.compile(script);
                scripts.put(script, cs);
            }
        }

        try {
            return Convert.toStr(cs.eval(new SimpleBindings(bindings)));
        } catch (ScriptException e) {
            throw new ScriptRuntimeException(e);
        }
    }
}