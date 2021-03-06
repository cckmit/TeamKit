package org.team4u.base.masker;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

/**
 * 基于位置的简单掩码器
 *
 * @author jay.wu
 */
public class SimpleMasker extends AbstractMasker {
    /**
     * 替换符号，默认为 *
     */
    private char replacedChar;
    /**
     * 开始位置（包含）
     */
    private int begin;
    /**
     * 结束位置（不包含）
     * <p>
     * -1代表结束位置 = value长度
     * -2代表结束位置 = value长度 - 1
     * 以此类推
     */
    private int end;

    /**
     * 若不满足长度，使用兜底掩码器
     */
    private Masker masker;

    public SimpleMasker(int begin, int end) {
        this('*', begin, end, null);
    }

    public SimpleMasker(int begin, int end, Masker masker) {
        this('*', begin, end, masker);
    }

    public SimpleMasker(char replacedChar, int begin, int end, Masker masker) {
        setReplacedChar(replacedChar);
        setMasker(masker);
        setBegin(begin);
        setEnd(end);
    }

    @Override
    public String internalMask(Object value) {
        String valueToMask = serializer().serializeToString(value);

        int b = actualPos(valueToMask.length(), begin());
        int e = actualPos(valueToMask.length(), end());

        String valueMasked = StrUtil.replace(valueToMask, b, e, replacedChar);

        // 没有掩码成功，使用兜底掩码器
        if (StrUtil.equals(valueMasked, valueToMask)) {
            if (masker != null) {
                return masker.mask(value);
            }
        }

        return valueMasked;
    }

    public char replacedChar() {
        return replacedChar;
    }

    public int begin() {
        return begin;
    }

    public int end() {
        return end;
    }

    private int actualPos(int length, int pos) {
        return pos < 0 ? length + pos + 1 : pos;
    }

    protected void setBegin(int begin) {
        this.begin = begin;
    }

    protected void setEnd(int end) {
        this.end = end;
    }

    protected void setReplacedChar(char replacedChar) {
        Assert.notNull(replacedChar, "replacedChar is required");
        this.replacedChar = replacedChar;
    }

    public Masker getMasker() {
        return masker;
    }

    protected void setMasker(Masker masker) {
        this.masker = masker;
    }
}
