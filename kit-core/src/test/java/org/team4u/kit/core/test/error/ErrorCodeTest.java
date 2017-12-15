package org.team4u.kit.core.test.error;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.error.ErrorCode;
import org.team4u.kit.core.error.ServiceException;
import org.team4u.kit.core.util.AssertUtil;

public class ErrorCodeTest {

    @Test
    public void create() {
        try {
            AssertUtil.error(ErrorCode.PARAM_ERROR, "a");
            Assert.fail();
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.PARAM_ERROR.getInnerCode(), e.getCode());
            Assert.assertEquals(String.format(ErrorCode.PARAM_ERROR.getMessage(), "a"), e.getMessage());
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
