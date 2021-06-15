package org.team4u.exporter.infrastructure.exporter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import org.junit.Test;
import org.team4u.exporter.domain.Title;

import java.io.ByteArrayOutputStream;

public class SimpleCsvExporterTest {

    private final SimpleCsvExporter exporter = new SimpleCsvExporter();

    @Test
    public void exportMap() {
        SimpleCsvExporter.Context c = new SimpleCsvExporter.Context();
        c.setOutputStream(System.out)
                .setTitles(CollUtil.newArrayList(
                        new Title("a", "1"),
                        new Title("b", "2")
                ))
                .setRows(CollUtil.newArrayList(
                        Dict.create().set("b", "22").set("a", "11")
                        )
                );
        exporter.export(c);
    }

    @Test
    public void exportBean() {
        SimpleCsvExporter.Context c = new SimpleCsvExporter.Context();
        c.setOutputStream(System.out)
                .setTitles(CollUtil.newArrayList(
                        new Title("a", "1"),
                        new Title("b", "2")
                ))
                .setRows(CollUtil.newArrayList(new TestBean("11", "22"))
                );

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        exporter.export(c);
    }
}