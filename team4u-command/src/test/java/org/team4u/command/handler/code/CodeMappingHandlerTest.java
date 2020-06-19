package org.team4u.command.handler.code;

import cn.hutool.core.collection.CollUtil;
import org.team4u.command.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.command.handler.HandlerAttributesKeys;
import org.team4u.core.lang.EasyMap;

public class CodeMappingHandlerTest {

    @Test
    public void buildForCode() {
        check("0", null, "1");
    }

    @Test
    public void buildForSubCode() {
        check("0", "1", "2");
    }

    @Test
    public void buildForDefaultCode() {
        check(null, null, "3");
    }

    private void check(String code, String subCode, String standardCode) {
        CodeMappingHandler handler = new CodeMappingHandler(
                TestUtil.newTemplateEngine(),
                TestUtil.newInterceptorService()
        );

        EasyMap attrs = new EasyMap()
                .set("code", code)
                .set("subCode", subCode);

        CodeMappingHandler.MappingConfig config =
                new CodeMappingHandler.MappingConfig()
                        .setCodeMappings(
                                CollUtil.newArrayList(
                                        new CodeMappingHandler.CodeMapping(
                                                code,
                                                subCode,
                                                standardCode
                                        )
                                )
                        )
                        .setCodeKey("code")
                        .setSubCodeKey("subCode")
                        .setStandardCodeKey(HandlerAttributesKeys.STANDARD_CODE);

        handler.internalHandle(config, attrs);

        Assert.assertEquals(standardCode, attrs.getStr(HandlerAttributesKeys.STANDARD_CODE));
    }
}