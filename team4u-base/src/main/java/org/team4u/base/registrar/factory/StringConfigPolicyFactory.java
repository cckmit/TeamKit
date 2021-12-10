package org.team4u.base.registrar.factory;

/**
 * 基于字符串配置的策略工厂
 *
 * @param <C> 转换后配置类型
 * @param <P> 策略类型
 * @author jay.wu
 */
public interface StringConfigPolicyFactory<P> extends PolicyFactory<String, P> {
}