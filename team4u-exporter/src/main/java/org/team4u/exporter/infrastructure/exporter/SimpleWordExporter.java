package org.team4u.exporter.infrastructure.exporter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Singleton;
import com.deepoove.poi.XWPFTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team4u.exporter.domain.Exporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 简单word导出器
 *
 * @author jay.wu
 */
public class SimpleWordExporter implements Exporter<SimpleWordExporter.Context> {

    public static SimpleWordExporter getInstance() {
        return Singleton.get(SimpleWordExporter.class);
    }

    @Override
    public void export(Context context) {
        XWPFTemplate template = XWPFTemplate.compile(context.getTemplateInputStream()).render(context.getBindings());

        try {
            template.writeAndClose(context.getOutputStream());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Context {
        /**
         * 字体路径集合
         */
        @Builder.Default
        private List<String> fonts = CollectionUtil.newArrayList("font/simsun.ttf");
        /**
         * 模板路径
         */
        private InputStream templateInputStream;
        /**
         * 模板引擎标识
         */
        @Builder.Default
        private String templateEngineId = "BEETL";
        /**
         * 上下文变量
         */
        private Map<String, Object> bindings;
        /**
         * 输出流
         */
        private OutputStream outputStream;
    }
}