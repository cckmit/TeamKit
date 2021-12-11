package org.team4u.selector.domain.selector;


import org.team4u.base.registrar.StringIdPolicy;
import org.team4u.base.registrar.factory.StringConfigPolicyFactory;

/**
 * 选择执行器构建工厂
 *
 * @author jay.wu
 */
public interface SelectorFactory extends StringConfigPolicyFactory<Selector>, StringIdPolicy {
}