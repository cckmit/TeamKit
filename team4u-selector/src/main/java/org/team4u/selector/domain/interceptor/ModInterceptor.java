package org.team4u.selector.domain.interceptor;

import cn.hutool.core.bean.BeanUtil;
import org.team4u.selector.domain.selector.SelectorResult;
import org.team4u.selector.domain.selector.binding.SelectorBinding;
import org.team4u.selector.domain.selector.binding.SimpleMapBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;

import java.util.Map;

/**
 * 取模拦截器
 *
 * @author jay.wu
 */
public class ModInterceptor implements SelectorInterceptor {

    private final Config config;

    public ModInterceptor(Config config) {
        this.config = config;
    }

    @Override
    public SelectorBinding preHandle(SelectorBinding binding) {
        if (binding == null) {
            return null;
        }

        if (binding instanceof SimpleMapBinding) {
            SimpleMapBinding newBinding = new SimpleMapBinding();

            for (Map.Entry<String, Object> entry : ((SimpleMapBinding) binding).entrySet()) {
                newBinding.put(entry.getKey(), mod(entry.getValue()));
            }

            return newBinding;
        }

        if (binding instanceof SingleValueBinding) {
            return new SingleValueBinding(mod(((SingleValueBinding) binding).value()));
        }

        return binding;
    }

    @Override
    public SelectorResult postHandle(SelectorBinding context, SelectorResult value) {
        return value;
    }

    /**
     * 取模算法
     */
    private int mod(Object value) {
        return Math.abs(value.hashCode()) % config.getCount();
    }

    public static class Config {
        /**
         * 取模范围
         */
        private int count;

        public int getCount() {
            return count;
        }

        public Config setCount(int count) {
            this.count = count;
            return this;
        }
    }

    /**
     * 取模拦截器构建工厂
     */
    public static class Factory implements SelectorInterceptorFactory {

        @Override
        public SelectorInterceptor create(Object config) {
            return new ModInterceptor(BeanUtil.toBean(config, Config.class));
        }

        @Override
        public String id() {
            return "mod";
        }
    }
}