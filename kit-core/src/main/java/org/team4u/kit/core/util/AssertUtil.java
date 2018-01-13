package org.team4u.kit.core.util;

import org.team4u.kit.core.error.ErrorCode;
import org.team4u.kit.core.error.ServiceException;

import java.util.Collection;

/**
 * 断言工具类
 */
public class AssertUtil {

    public static void error(ErrorCode errorCode, Object... fmt) {
        throw new ServiceException(errorCode.getInnerCode(), fmt != null ?
                String.format(errorCode.getMessage(), fmt) : errorCode.getMessage());
    }

    public static void isTrue(ErrorCode errorCode, boolean cnd, Object... fmt) {
        if (!cnd) {
            error(errorCode, fmt);
        }
    }

    public static void isFalse(ErrorCode errorCode, boolean cnd, Object... fmt) {
        isTrue(errorCode, !cnd, fmt);
    }

    public static void isEmpty(ErrorCode errorCode, Object cnd, Object... fmt) {
        if (!ValueUtil.isEmpty(cnd)) {
            error(errorCode, fmt);
        }
    }

    public static void isNotEmpty(ErrorCode errorCode, Object cnd, Object... fmt) {
        if (ValueUtil.isEmpty(cnd)) {
            error(errorCode, fmt);
        }
    }

    public static void isUnique(ErrorCode errorCode, Collection collection, Object... fmt) {
        isNotEmpty(errorCode, collection, fmt);
        isTrue(errorCode, collection.size() == 1, fmt);
    }

    public static void validate(ErrorCode errorCode, Object obj) {
        ValidatorUtil.Result result = ValidatorUtil.validate(obj);
        if (result.hasError()) {
            error(errorCode, result.firstMessage());
        }
    }
}