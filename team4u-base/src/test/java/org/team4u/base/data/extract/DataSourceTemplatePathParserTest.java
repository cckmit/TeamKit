package org.team4u.base.data.extract;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.data.extract.DataSourceTemplatePathParser;

import java.util.List;
import java.util.Map;

public class DataSourceTemplatePathParserTest {

    private DataSourceTemplatePathParser pathParser = new DataSourceTemplatePathParser();

    @Test
    public void bean() {
        Map<String, List<String>> paths = pathParser.parseTemplatePathValues(loadJson(
                "data/extract/parser_map_template.json"));
        Assert.assertEquals("1", paths.get("a.b.c").get(0));
        Assert.assertEquals("2", paths.get("a2.b2").get(0));
    }

    @Test
    public void map() {
        Map<String, List<String>> paths = pathParser.parseTemplatePathValues(loadJson("data/extract/parser_map_template.json"));
        Assert.assertEquals("1", paths.get("a.b.c").get(0));
        Assert.assertEquals("2", paths.get("a2.b2").get(0));
    }

    @Test
    public void list() {
        Map<String, List<String>> paths = pathParser.parseTemplatePathValues(loadJson("data/extract/parser_list_template.json"));
        List<String> result = paths.get("[].a.b.c");
        Assert.assertEquals("1", result.get(0));
        Assert.assertEquals("2", result.get(1));
    }

    private Object loadJson(String path) {
        return JSON.parse(FileUtil.readUtf8String(path));
    }
}