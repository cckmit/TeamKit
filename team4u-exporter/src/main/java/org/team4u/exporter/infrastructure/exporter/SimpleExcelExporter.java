package org.team4u.exporter.infrastructure.exporter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.team4u.exporter.domain.Exporter;
import org.team4u.exporter.domain.Title;

/**
 * 简单excel导出器
 *
 * @author jay.wu
 */
public class SimpleExcelExporter implements Exporter<RowContext> {

    private static final SimpleExcelExporter INSTANCE = new SimpleExcelExporter();

    public static SimpleExcelExporter getInstance() {
        return INSTANCE;
    }

    @Override
    public void export(RowContext context) {
        ExcelWriter writer = createExcelWriter(context);

        writeHeader(writer, context);
        writeBody(writer, context);

        writer.flush(context.getOutputStream(), context.isCloseOut());
        writer.close();
    }

    private void writeBody(ExcelWriter writer, RowContext context) {
        writer.write(context.getRows());
    }

    private void writeHeader(ExcelWriter writer, RowContext context) {
        if (!context.isWriteTitle()) {
            return;
        }

        if (CollUtil.isEmpty(context.getTitles())) {
            return;
        }

        for (Title title : context.getTitles()) {
            writer.addHeaderAlias(title.getKey(), title.getName());
        }
    }

    protected ExcelWriter createExcelWriter(RowContext context) {
        ExcelWriter writer = ExcelUtil.getBigWriter();
        writer.setOnlyAlias(true);
        return writer;
    }
}