package org.team4u.base.masker;

import cn.hutool.core.lang.Validator;

/**
 * 姓名掩码器
 * <p>
 * 中文姓名，三个字及以下，只显示最后一个字 ;三个字以上，显示最后两个字，如：*明;*小明
 * 英文姓名，只显示前一后一，如：a***b
 *
 * @author jay.wu
 */
public class NameMasker extends AbstractMasker {

    @Override
    public String internalMask(Object value) {
        String valueToMask = serializer().serializeToString(value);

        if (Validator.hasChinese(valueToMask)) {
            return maskChineseName(valueToMask);
        } else {
            return maskOtherName(valueToMask);
        }
    }

    private String maskOtherName(String valueToMask) {
        return new SimpleMasker(1, -2).mask(valueToMask);
    }

    private String maskChineseName(String valueToMask) {
        if (valueToMask.length() <= 3) {
            return new SimpleMasker(0, -2).mask(valueToMask);
        } else {
            return new SimpleMasker(0, -3).mask(valueToMask);
        }
    }
}