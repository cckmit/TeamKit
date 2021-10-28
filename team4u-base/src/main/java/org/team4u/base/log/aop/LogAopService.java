package org.team4u.base.log.aop;

/**
 * 日志aop打印服务、
 * <p>
 * 可基于aop的方式打印方法的输入、输出信息
 *
 * @author jay.wu
 */
public class LogAopService {

    /**
     * 生成可打印日志的代理类，底层使用Spring和Cglib
     *
     * @param target 原始对象
     * @param <T>    代理类型
     * @return 代理类
     */
    public static <T> T proxy(T target) {
        return proxy(target, new LogAop.Config());
    }

    /**
     * 生成可打印日志的代理类，底层使用Spring和Cglib
     *
     * @param target 原始对象
     * @param config 配置
     * @param <T>    代理类型
     * @return 代理类
     */
    public static <T> T proxy(T target, LogAop.Config config) {
        return new ByteBuddyLogAop().proxy(target, config);
    }
}