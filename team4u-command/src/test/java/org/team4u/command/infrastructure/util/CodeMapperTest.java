package org.team4u.command.infrastructure.util;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CodeMapperTest {
    private final CodeMapper mapper = new CodeMapper();

    @Test
    public void map() {
        String code = mapper.map(codeMappings(), null, null);
        Assert.assertEquals("1", code);

         code = mapper.map(codeMappings(), "2", "2");
        Assert.assertEquals("2", code);

        code = mapper.map(codeMappings(), "3", "3");
        Assert.assertEquals("3", code);

        code = mapper.map(codeMappings(), null, "4");
        Assert.assertEquals("4", code);

        code = mapper.map(codeMappings(), "1", null);
        Assert.assertEquals("0", code);
    }

    private List<CodeMapper.CodeMapping> codeMappings() {
        return CollUtil.newArrayList(
                new CodeMapper.CodeMapping(null, null, "0"),
                new CodeMapper.CodeMapping("-", null, "1"),
                new CodeMapper.CodeMapping("2", null, "2"),
                new CodeMapper.CodeMapping("3", "3", "3"),
                new CodeMapper.CodeMapping("-", "4", "4")
        );
    }
}