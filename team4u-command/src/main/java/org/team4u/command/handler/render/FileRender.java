package org.team4u.command.handler.render;

import cn.hutool.core.io.FileUtil;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.command.handler.HandlerConfig;
import org.team4u.core.lang.EasyMap;
import org.team4u.template.TemplateEngine;

/**
 * 文件渲染器
 *
 * @author jay.wu
 */
public class FileRender extends AbstractDefaultHandler<HandlerConfig, Void> {
    public FileRender(TemplateEngine templateEngine,
                      HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected Void internalHandle(HandlerConfig config, EasyMap attributes) {
        String source = sourceOf(config, attributes, String.class);
        FileUtil.writeUtf8String(source, config.getTargetKey());
        return null;
    }

    @Override
    public String id() {
        return "fileRender";
    }
}