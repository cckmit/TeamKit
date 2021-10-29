package org.team4u.base.log.aop;

import cn.hutool.log.Log;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team4u.base.log.LogMessageConfig;
import org.team4u.base.registrar.StringIdPolicy;

import java.util.Collections;
import java.util.List;

/**
 * 日志aop打印
 *
 * @author jay.wu
 */
public interface LogAop extends StringIdPolicy {

    /**
     * 生成可打印日志的代理类，底层使用Spring和Cglib
     *
     * @param target 原始对象
     * @param <T>    代理类型
     * @return 代理类
     */
    default <T> T proxy(T target) {
        return proxy(target, new Config());
    }

    /**
     * 生成可打印日志的代理类，底层使用Spring和Cglib
     *
     * @param target 原始对象
     * @param config 配置
     * @param <T>    代理类型
     * @return 代理类
     */
    <T> T proxy(T target, Config config);

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Config {
        private String logAopId;
        /**
         * 不打印输入
         * <p>
         * 用于避免打印敏感信息
         */
        @Builder.Default
        private boolean inputEnabled = true;
        /**
         * 不打印输出
         * <p>
         * 用于避免打印敏感信息
         */
        @Builder.Default
        private boolean outputEnabled = true;
        /**
         * 是否开启打印日志功能
         */
        @Builder.Default
        private boolean enabled = true;
        /**
         * 日志消息配置
         */
        private LogMessageConfig logMessageConfig;
        /**
         * 不需要需要打印的方法集合
         */
        @Builder.Default
        private List<String> excludedMethods = Collections.emptyList();
        /**
         * 需要打印的方法集合
         */
        @Builder.Default
        private List<String> includedMethods = Collections.emptyList();

        @Builder.Default
        private Log log = Log.get(LogAop.class);
    }
}