package org.team4u.base.log;

import org.team4u.base.log.aop.LogAop;
import org.team4u.base.log.aop.LogAopService;

/**
 * 日志跟踪代理，可打印方法的输入、输出信息
 *
 * @author jay.wu
 * @see org.team4u.base.log.aop.LogAopService
 * @deprecated 使用LogAopService代替
 */
public class LogTraceProxyFactory {

    /**
     * 生成可打印日志的代理类
     *
     * @param target 原始对象
     * @param <T>    代理类型
     * @return 代理类
     */
    public static <T> T proxy(T target) {
        return LogAopService.getInstance().proxy(target);
    }

    /**
     * 生成可打印日志的代理类
     *
     * @param target 原始对象
     * @param config 配置
     * @param <T>    代理类型
     * @return 代理类
     */
    public static <T> T proxy(T target, LogAop.Config config) {
        return LogAopService.getInstance().proxy(target, config);
    }
}