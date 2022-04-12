package org.team4u.exporter.infrastructure.exporter;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import org.junit.Test;

public class SimpleWordExporterTest {

    @Test
    public void export() {
        SimpleWordExporter.getInstance().export(SimpleWordExporter.Context.builder()
                .templateInputStream(FileUtil.getInputStream("template/test.docx"))
                .outputStream(FileUtil.getOutputStream("result/test.docx"))
                .bindings(Dict.create().set("title", "选中的复选框：☑，未选中的复选框：□"))
                .build());
    }
}