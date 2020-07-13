package org.team4u.base.data.extract;

import cn.hutool.core.io.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.team4u.base.TestUtil.loadJsonObject;


public class DataTargetTemplateExtracterTest {

    @Test
    public void extractToBean() {
        DataTargetTemplateExtracter extracter2 = new DataTargetTemplateExtracter(new FastJsonDataTargetTemplateSerializer());
        @SuppressWarnings("unchecked") Map<String, Object> x = extracter2.extractToBean(
                FileUtil.readUtf8String("data/extract/simple_list_extracter_template2.json"),
                loadJsonObject("data/extract/simple_list_extracter_data.json"),
                Map.class
        );

        Assert.assertEquals(
                "{\"x\":[\"fjay\",\"blue\"],\"y\":[12,5],\"z\":{\"z0\":\"fjay\",\"z1\":[null,\"1\"]}}",
                x.toString()
        );
    }

    @Test
    public void extractToList() {
        DataTargetTemplateExtracter extracter2 = new DataTargetTemplateExtracter(new FastJsonDataTargetTemplateSerializer());
        List<Object> x = extracter2.extractToList(
                FileUtil.readUtf8String("data/extract/simple_list_extracter_template3.json"),
                loadJsonObject("data/extract/simple_list_extracter_data.json"),
                Object.class
        );

        Assert.assertEquals("[[fjay, blue], [12, 5], fjay, null]", x.toString());
    }
}