package org.team4u.core.masker;

import cn.hutool.core.util.StrUtil;

/**
 * 电子邮箱掩码器
 *
 * @author jay.wu
 */
public class EmailMasker extends SimpleMasker {

    public EmailMasker() {
        this(2, -1);
    }

    public EmailMasker(int begin, int end) {
        this('*', begin, end);
    }

    public EmailMasker(char replacedChar, int begin, int end) {
        super(replacedChar, begin, end);
    }

    @Override
    public String mask(Object value) {
        String stringValue = serializer().serializeToString(value);
        int pos = stringValue.indexOf("@");
        if (pos < 1) {
            return stringValue;
        }

        String valueToMask = StrUtil.subPre(stringValue, pos);
        return super.mask(valueToMask) + StrUtil.subSuf(stringValue, pos);
    }
}