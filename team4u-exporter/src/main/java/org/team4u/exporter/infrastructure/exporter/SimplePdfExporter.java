package org.team4u.exporter.infrastructure.exporter;

import cn.hutool.core.collection.CollectionUtil;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team4u.base.lang.lazy.LazySupplier;
import org.team4u.exporter.domain.Exporter;
import org.team4u.template.TemplateEngines;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 简单pdf导出器
 *
 * @author jay.wu
 */
public class SimplePdfExporter implements Exporter<SimplePdfExporter.Context> {

    private static final LazySupplier<SimplePdfExporter> instance = LazySupplier.of(SimplePdfExporter::new);

    private final TemplateEngines templateEngines = new TemplateEngines();

    public static SimplePdfExporter getInstance() {
        return instance.get();
    }

    @Override
    public void export(Context context) throws Exception {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(renderHtml(context));
        initFont(renderer, context);
        renderer.layout();
        renderer.createPDF(context.getOutputStream());

        context.getOutputStream().flush();
    }

    private void initFont(ITextRenderer renderer, Context context) throws DocumentException, IOException {
        for (String font : context.getFonts()) {
            renderer.getFontResolver().addFont(font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        }
    }

    private String renderHtml(Context context) {
        return templateEngines.availablePolicyOf(context.templateEngineId)
                .render(context.getTemplate(), context.getBindings());
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
         * 模板内容
         */
        private String template;
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