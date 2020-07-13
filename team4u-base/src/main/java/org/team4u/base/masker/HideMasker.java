package org.team4u.base.masker;

/**
 * 全部隐藏掩码器
 * <p>
 * 结果固定返回一位长度的replacedChar
 *
 * @author jay.wu
 */
public class HideMasker extends AbstractMasker {

    private final char replacedChar;

    public HideMasker() {
        this('*');
    }

    public HideMasker(char replacedChar) {
        this.replacedChar = replacedChar;
    }

    public HideMasker(MaskerValueSerializer serializer, char replacedChar) {
        super(serializer);
        this.replacedChar = replacedChar;
    }

    @Override
    public String internalMask(Object value) {
        return serializer().serializeToString(replacedChar);
    }
}
