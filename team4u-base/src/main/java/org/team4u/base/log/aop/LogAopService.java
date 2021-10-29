package org.team4u.base.log.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 日志aop打印服务、
 * <p>
 * 可基于aop的方式打印方法的输入、输出信息
 *
 * @author jay.wu
 */
public class LogAopService extends PolicyRegistrar<String, LogAop> {

    private static final LogAopService instance = new LogAopService();

    public static LogAopService getInstance() {
        return instance;
    }

    /**
     * 生成可打印日志的代理类，底层使用Spring和Cglib
     *
     * @param target 原始对象
     * @param <T>    代理类型
     * @return 代理类
     */
    public <T> T proxy(T target) {
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
    public <T> T proxy(T target, LogAop.Config config) {
        LogAop aop = ObjectUtil.defaultIfNull(
                policyOf(config.getLogAopId()), CollUtil.getFirst(policies())
        );

        if (aop == null) {
            return target;
        }

        return aop.proxy(target, config);
    }
}