package org.team4u.base.masker;

import cn.hutool.core.util.StrUtil;

/**
 * 基于百分比的掩码器
 * <p>
 * 结果按百分比对值的后半部分进行掩码
 * <p>
 * 如 percentToMask=0.8,value=abcd,掩码结果为a***
 *
 * @author jay.wu
 */
public class PercentageMasker extends AbstractMasker {

    private final double percentToMask;
    private final int limit;

    public PercentageMasker(double percentToMask) {
        this(percentToMask, -1);
    }

    /**
     * 百分比掩码器
     *
     * @param percentToMask 掩码百分比
     * @param limit         结束索引值，从0开始，-1表示最后位置
     */
    public PercentageMasker(double percentToMask, int limit) {
        this.percentToMask = percentToMask;
        this.limit = limit;
    }

    @Override
    public String internalMask(Object value) {
        String valueToMask = serializer().serializeToString(value);

        if (limit > -1) {
            valueToMask = StrUtil.sub(valueToMask, 0, limit);
        }

        int begin = countBeginIndex(valueToMask);
        return new SimpleMasker(begin, -1).mask(valueToMask);
    }

    private int countBeginIndex(String valueToMask) {
        int totalLength = valueToMask.length();
        int lengthToMask = (int) (percentToMask * totalLength);
        return totalLength - lengthToMask;
    }
}