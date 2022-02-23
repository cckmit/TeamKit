package org.team4u.command.infrastructure.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import org.junit.Assert;
import org.junit.Test;

public class XmlExtractorTest {

    @Test
    public void extractFor() {
        XmlExtractor extractor = new XmlExtractor();
        Object source = extractor.extractableSource(FileUtil.readUtf8String("test1.xml"));
        Assert.assertEquals("40", BeanUtil.getProperty(source, "ROW.OUTPUT.ITEM.result_fs"));
        Assert.assertEquals("1", BeanUtil.getProperty(source, "ROW.OUTPUT2.ITEM[0].errormesage"));
        Assert.assertEquals("2", BeanUtil.getProperty(source, "ROW.OUTPUT2.ITEM[1].errormesagecol"));
    }
}