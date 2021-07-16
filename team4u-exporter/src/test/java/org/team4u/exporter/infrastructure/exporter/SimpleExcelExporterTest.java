package org.team4u.exporter.infrastructure.exporter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.poi.excel.ExcelUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.exporter.domain.Title;

import java.util.List;

public class SimpleExcelExporterTest {

    private final SimpleExcelExporter exporter = new SimpleExcelExporter();

    @Test
    public void exportMap() {
        exporter.export(new RowContext()
                .setOutputStream(FileUtil.getOutputStream("test.xls"))
                .setTitles(CollUtil.newArrayList(
                        new Title("a", "1"),
                        new Title("b", "2")
                ))
                .setRows(CollUtil.newArrayList(
                        Dict.create().set("b", "22").set("a", "11").set("c", "33"))
                )
        );

        List<TestBean> data = ExcelUtil.getReader("test.xls").readAll(TestBean.class);
        Assert.assertEquals("[1122]", data.toString());
    }

    @Test
    public void exportBean() {
        RowContext c = new RowContext()
                .setOutputStream(FileUtil.getOutputStream("test.xls"))
                .setRows(CollUtil.newArrayList(new TestBean("11", "22")));

        exporter.export(c);

        List<TestBean> data = ExcelUtil.getReader("test.xls").readAll(TestBean.class);
        Assert.assertEquals("[1122]", data.toString());
    }
}