package org.team4u.base.registrar;

/**
 * 策略
 *
 * @author jay.wu
 */
public interface Policy<C> {
    /**
     * 最高优先级
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    /**
     * 最低优先级
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * 是否策略支持处理该上下文
     *
     * @param context 　上下文
     */
    boolean isSupport(C context);

    /**
     * 优先级
     * <p>
     * 数值越小，优先级越高
     */
    default int priority() {
        return LOWEST_PRECEDENCE;
    }

    /**
     * 获取策略名称
     *
     * @return 策略名称
     */
    default String policyName() {
        return getClass().getName();
    }
}