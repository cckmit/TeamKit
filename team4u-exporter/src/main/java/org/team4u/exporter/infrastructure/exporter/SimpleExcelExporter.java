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

    @Override
    public void export(RowContext context) {
        ExcelWriter writer = createExcelWriter(context);

        writeHeader(writer, context);
        writer.write(context.getRows());

        writer.flush(context.getOutputStream(), context.isCloseOut());
        writer.close();
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
        return ExcelUtil.getBigWriter();
    }
}