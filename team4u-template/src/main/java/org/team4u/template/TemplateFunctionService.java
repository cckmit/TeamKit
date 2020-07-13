package org.team4u.template;



import org.team4u.base.lang.IdObjectService;

import java.util.List;

/**
 * 自定义模板函数服务
 *
 * @author jay.wu
 */
public class TemplateFunctionService extends IdObjectService<String, TemplateFunction> {

    public TemplateFunctionService() {
    }

    public TemplateFunctionService(List<TemplateFunction> functions) {
        super(functions);
    }
}