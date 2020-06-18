package org.team4u.core.masker;

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

    public PercentageMasker(double percentToMask) {
        this.percentToMask = percentToMask;
    }

    @Override
    public String internalMask(Object value) {
        String valueToMask = serializer().serializeToString(value);

        int begin = countBeginIndex(valueToMask);

        return new SimpleMasker(begin, -1).mask(value);
    }

    private int countBeginIndex(String valueToMask) {
        int totalLength = valueToMask.length();
        int lengthToMask = (int) (percentToMask * totalLength);
        return totalLength - lengthToMask;
    }
}