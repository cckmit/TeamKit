package org.team4u.command.infrastructure.util;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.command.infrastructure.util.CodeMapper.CodeMapping;

import java.util.List;

public class CodeMapperTest {
    private final CodeMapper mapper = new CodeMapper();

    @Test
    public void map() {
        check(null, null, "1");

        check("2", "2", "2");

        check("3", "3", "3");

        check(null, "4", "4");

        check("1", null, "0");

        // 正则表达式
        check("131", "4", "5");
        check("121", "4", "5");
        check("1221", "4", "0");
        check("11", "4", "0");
    }

    private void check(String originalCode, String originalSubCode, String standardCode) {
        Assert.assertEquals(standardCode, mapper.map(codeMappings(), originalCode, originalSubCode));
    }

    private List<CodeMapping> codeMappings() {
        return CollUtil.newArrayList(
                new CodeMapping(null, null, "0"),
                new CodeMapping("-", null, "1"),
                new CodeMapping("2", null, "2"),
                new CodeMapping("3", "3", "3"),
                new CodeMapping("-", "4", "4"),
                new CodeMapping("1.1", "4", "5")
        );
    }
}