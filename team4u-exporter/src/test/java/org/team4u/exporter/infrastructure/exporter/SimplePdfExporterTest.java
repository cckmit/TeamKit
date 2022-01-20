package org.team4u.exporter.infrastructure.exporter;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class SimplePdfExporterTest {

    @Test
    public void export() throws Exception {
        ByteArrayOutputStream o = new ByteArrayOutputStream();

        SimplePdfExporter.getInstance().export(
                SimplePdfExporter.Context.builder()
                        .template(FileUtil.readUtf8String("template/test.html"))
                        .bindings(Dict.create().set("a", "1"))
                        .outputStream(o)
                        .build()
        );

        FileUtil.writeBytes(o.toByteArray(), "test.pdf");
    }
}