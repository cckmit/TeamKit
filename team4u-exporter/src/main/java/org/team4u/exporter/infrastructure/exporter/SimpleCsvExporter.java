package org.team4u.exporter.infrastructure.exporter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriteConfig;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.ReflectUtil;
import org.team4u.exporter.domain.Exporter;
import org.team4u.exporter.domain.Title;

import java.io.OutputStreamWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 简单csv导出器
 *
 * @author jay.wu
 */
public class SimpleCsvExporter implements Exporter<SimpleCsvExporter.Context> {

    @Override
    public void export(Context context) {
        CsvWriter writer = CsvUtil.getWriter(
                new OutputStreamWriter(context.getOutputStream()),
                context.getCsvWriteConfig());

        writeHeader(writer, context);
        writeBody(writer, context);

        writer.flush();
        writer.close();
    }

    private void writeHeader(CsvWriter writer, RowContext context) {
        if (!context.isWriteTitle()) {
            return;
        }

        if (CollUtil.isEmpty(context.getTitles())) {
            return;
        }

        writer.write(
                Collections.singleton(context.getTitles()
                        .stream()
                        .map(Title::getName)
                        .collect(Collectors.toList()))
        );
    }

    @SuppressWarnings("rawtypes")
    private void writeBody(CsvWriter writer, RowContext context) {
        List<?> rows = context.getRows()
                .stream()
                .map(it -> {
                    if (it instanceof Map) {
                        return writeMap(context, (Map<?, ?>) it);
                    }

                    if (BeanUtil.isBean(it.getClass())) {
                        return writeBean(context, it);
                    }

                    return it;
                })
                .collect(Collectors.toList());

        writer.write(rows);
    }

    private Collection<?> writeBean(RowContext context, Object bean) {
        if (CollUtil.isEmpty(context.getTitles())) {
            return Arrays.stream(ReflectUtil.getFields(bean.getClass()))
                    .map(field -> ReflectUtil.getFieldValue(bean, field))
                    .collect(Collectors.toList());
        }

        return context.getTitles().stream()
                .map(title -> BeanUtil.getProperty(bean, title.getKey()))
                .collect(Collectors.toList());
    }

    private Collection<?> writeMap(RowContext context, Map<?, ?> map) {
        if (CollUtil.isEmpty(context.getTitles())) {
            return map.values();
        }

        return context.getTitles().stream()
                .map(title -> map.get(title.getKey()))
                .collect(Collectors.toList());
    }

    public static class Context extends RowContext {

        private CsvWriteConfig csvWriteConfig;

        public CsvWriteConfig getCsvWriteConfig() {
            return csvWriteConfig;
        }

        public Context setCsvWriteConfig(CsvWriteConfig csvWriteConfig) {
            this.csvWriteConfig = csvWriteConfig;
            return this;
        }
    }
}