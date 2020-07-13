package org.team4u.command.handler.render;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriteConfig;
import cn.hutool.core.text.csv.CsvWriter;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.command.handler.HandlerConfig;
import org.team4u.base.lang.EasyMap;
import org.team4u.template.TemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * csv渲染器
 *
 * @author jay.wu
 */
public class CsvRender extends AbstractDefaultHandler<HandlerConfig, String> {

    public CsvRender(TemplateEngine templateEngine,
                     HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected String internalHandle(HandlerConfig config, EasyMap attributes) {
        List<Map<?, ?>> source = sourceOf(config, attributes, List.class);
        return toCsvString(headers(source), values(source));
    }

    private List<Set<?>> headers(List<Map<?, ?>> source) {
        if (CollUtil.isEmpty(source)) {
            return Collections.emptyList();
        }

        List<Set<?>> headers = new ArrayList<>();
        headers.add(CollUtil.getFirst(source).keySet());
        return headers;
    }

    private List<Collection<?>> values(List<Map<?, ?>> source) {
        return source.stream()
                .map(Map::values)
                .collect(Collectors.toList());
    }

    private String toCsvString(List<Set<?>> headers, List<Collection<?>> values) {
        OutputStream out = new ByteArrayOutputStream();
        CsvWriter writer = CsvUtil.getWriter(IoUtil.getUtf8Writer(out), new CsvWriteConfig());

        writer.write(headers);
        writer.write(values);
        writer.flush();
        return out.toString();
    }

    @Override
    public String id() {
        return "csvRender";
    }
}