package org.team4u.base.log;

import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;

public class MaskableLogMessageTest {

    @Test
    public void appendWithMask() {
        LogMessage lm = lm();
        lm.append("z", "1234");
        Assert.assertEquals("x|y|z=*", lm.toString());
    }

    @Test
    public void setKeyValuesWithMask() {
        LogMessage lm = lm();
        lm.setKeyValues(Dict.create().set("z", "1234"));
        Assert.assertEquals("x|y|z=*", lm.toString());
    }

    @Test
    public void appendWithoutMask() {
        LogMessage lm = lm();
        lm.append("z0", "1234");
        Assert.assertEquals("x|y|z0=1234", lm.toString());
    }

    @Test
    public void globalMask() {
        LogMessage lm = LogMessages.createWithMasker("x", "y2");
        lm.append("name", "1234");
        Assert.assertEquals("x|y2|name=*", lm.toString());
    }

    private LogMessage lm() {
        return LogMessages.createWithMasker("x", "y");
    }
}