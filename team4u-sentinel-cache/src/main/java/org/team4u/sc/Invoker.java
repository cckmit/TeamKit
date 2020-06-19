package org.team4u.sc;

/**
 * 执行者
 *
 * @author jay.wu
 */
public interface Invoker {

    /**
     * 执行逻辑
     */
    Object invoke() throws Throwable;
}
