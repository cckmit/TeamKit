package org.team4u.core.masker;

import java.util.HashMap;
import java.util.Map;


/**
 * 掩码器集合
 *
 * @author jay.wu
 */
public class Maskers {

    private static final Maskers instance = new Maskers();

    private final Map<String, Masker> maskerMap = new HashMap<>();

    protected Maskers() {
        initDefaultMaskers();
    }

    public static Maskers instance() {
        return instance;
    }

    /**
     * 注册掩码器
     */
    public void register(String id, Masker masker) {
        maskerMap.put(id, masker);
    }

    /**
     * 根据掩码器标识查找掩码器
     */
    public Masker maskerOf(String id) {
        return maskerMap.get(id);
    }

    /**
     * 初始化默认掩码器
     */
    protected void initDefaultMaskers() {
        for (Type value : Type.values()) {
            register(value.name(), value.masker());
        }
    }

    /**
     * 常用掩码器类型
     */
    public enum Type {
        // 姓名(仅对最后一位掩码)
        NAME(new SimpleMasker(-2, -1)),
        // 手机号码(保留前3后4)
        MOBILE(new SimpleMasker(3, -5)),
        // 银行卡号(保留前6后4)
        BANK_CARD_NO(new SimpleMasker(6, -5)),
        // 身份证号(保留前6后4)
        ID_CARD_NO(new SimpleMasker(6, -5)),
        // 对值的66%部分（2/3）掩码
        PERCENT66(new PercentageMasker(0.66)),
        // 电子邮箱（保留前2）
        EMAIL(new EmailMasker()),
        // 不进行掩码
        NONE(new NoneMasker()),
        // 全部掩码
        HIDE(new HideMasker());

        private final Masker masker;

        Type(Masker masker) {
            this.masker = masker;
        }

        public Masker masker() {
            return masker;
        }

        public String mask(Object value) {
            return masker().mask(value);
        }
    }
}