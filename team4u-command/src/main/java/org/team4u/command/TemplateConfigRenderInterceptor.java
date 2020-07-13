package org.team4u.command;

import com.alibaba.fastjson.JSON;
import org.team4u.base.lang.EasyMap;
import org.team4u.template.TemplateEngine;

public class TemplateConfigRenderInterceptor implements HandlerInterceptor {

    private final TemplateEngine templateEngine;

    public TemplateConfigRenderInterceptor(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean preHandle(Handler.Context context, Handler filter) throws Exception {
        EasyMap config = renderConfig(context);

        return true;
    }

    @Override
    public void postHandle(Handler.Context context, Handler filter) throws Exception {

    }

    @Override
    public void afterCompletion(Handler.Context context, Handler filter, Exception ex) throws Exception {

    }

    @Override
    public String id() {
        return null;
    }

    private EasyMap renderConfig(Handler.Context context) {
        EasyMap currentConfig = context.config().configOf(id());
        if (currentConfig == null) {
            return new EasyMap();
        }

        String configStr = templateEngine.render(JSON.toJSONString(currentConfig), context.attributes());

        return JSON.parseObject(configStr, EasyMap.class);
    }
}
