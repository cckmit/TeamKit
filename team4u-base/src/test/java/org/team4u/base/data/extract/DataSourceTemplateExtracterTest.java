package org.team4u.base.data.extract;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.team4u.base.TestUtil.loadJsonObject;

public class DataSourceTemplateExtracterTest {

    @Test
    public void simpleBean() {
        DataSourceTemplateExtracter extracter = new DataSourceTemplateExtracter(loadJsonObject(
                "data/extract/simple_bean_extracter_template.json"));

        A a = new A();
        extracter.extract(
                new A()
                        .setX(CollUtil.newArrayList(1, 2, 3))
                        .setY(CollUtil.newArrayList("fjay", "blue")),
                a);

        Assert.assertEquals(CollUtil.newArrayList(1, 2, 3), a.x);
        Assert.assertEquals(CollUtil.newArrayList("fjay", "blue"), a.y);
    }

    @Test
    public void simpleMap() {
        DataSourceTemplateExtracter extracter = new DataSourceTemplateExtracter(loadJsonObject(
                "data/extract/simple_map_extracter_template.json"));

        A a = new A();
        extracter.extract(loadJsonObject("data/extract/simple_map_extracter_data.json"), a);

        Assert.assertEquals(CollUtil.newArrayList(1, 2, 3), a.x);
    }

    @Test
    public void simpleList() {
        DataSourceTemplateExtracter extracter = new DataSourceTemplateExtracter(loadJsonObject(
                "data/extract/simple_list_extracter_template.json"));

        A a = new A();
        extracter.extract(loadJsonObject("data/extract/simple_list_extracter_data.json"), a);

        Assert.assertEquals(CollUtil.newArrayList(12, 5), a.x);
        Assert.assertEquals(CollUtil.newArrayList("fjay", "blue"), a.y);
    }

    private static class A {

        private List<Integer> x;

        private List<String> y;

        public List<Integer> getX() {
            return x;
        }

        public A setX(List<Integer> x) {
            this.x = x;
            return this;
        }

        public List<String> getY() {
            return y;
        }

        public A setY(List<String> y) {
            this.y = y;
            return this;
        }
    }
}
