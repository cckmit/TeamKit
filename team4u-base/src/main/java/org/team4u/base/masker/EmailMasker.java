package org.team4u.base.masker;

import cn.hutool.core.util.StrUtil;

/**
 * 电子邮箱掩码器
 * <p>
 * 1、@前面的字符显示前3位，3位后显示3个*，@后面完整显示，如：fja***@126.com
 * 2、如果@少于三位，则全部显示，@前加***，如例如tt@111.com，则显示为fj***@126.com
 *
 * @author jay.wu
 */
public class EmailMasker extends AbstractMasker {

    private static final String REPLACED_CHAR = "***";

    @Override
    public String internalMask(Object value) {
        String stringValue = serializer().serializeToString(value);
        int pos = stringValue.indexOf("@");
        if (pos < 1) {
            return stringValue;
        }

        String valueToMask = StrUtil.subPre(stringValue, pos);
        String name;
        if (valueToMask.length() < 3) {
            name = valueToMask + REPLACED_CHAR;
        } else {

            name = StrUtil.subPre(stringValue, 3) + REPLACED_CHAR;
        }

        return name + StrUtil.subSuf(stringValue, pos);
    }
}