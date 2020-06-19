package org.team4u.sc;

import cn.hutool.core.util.ArrayUtil;

public class FakeSystemErrorDecider implements SystemErrorDecider {

    @Override
    public boolean isSystemError(Object result, String resultCodeKey, String[] systemErrorCodes) {
        return ArrayUtil.contains(systemErrorCodes, result.toString());
    }
}
