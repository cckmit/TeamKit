package org.team4u.command.handler.loader;

import cn.hutool.core.io.FileUtil;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.command.handler.HandlerConfig;
import org.team4u.base.lang.EasyMap;
import org.team4u.template.TemplateEngine;

/**
 * 文件加载器
 *
 * @author jay.wu
 */
public class FileLoader extends AbstractDefaultHandler<HandlerConfig, String> {
    public FileLoader(TemplateEngine templateEngine,
                      HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected String internalHandle(HandlerConfig config, EasyMap attributes) {
        String source = sourceOf(config, attributes, String.class);
        return FileUtil.readUtf8String(source);
    }

    @Override
    public String id() {
        return "fileLoader";
    }
}