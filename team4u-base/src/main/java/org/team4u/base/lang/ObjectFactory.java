package org.team4u.base.lang;

/**
 * 对象构建工厂
 *
 * @author jay.wu
 */
public interface ObjectFactory<O, C> extends IdObject<String> {

    /**
     * 创建对象
     *
     * @param config 配置类
     * @return 对象
     */
    O create(C config);
}