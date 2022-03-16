package org.team4u.selector.domain.interceptor;

import org.team4u.selector.domain.selector.SelectorResult;
import org.team4u.selector.domain.selector.binding.SelectorBinding;

import java.util.List;

/**
 * 选择执行器工厂服务
 *
 * @author jay.wu
 */
public class SelectorInterceptorService {

    public SelectorBinding preHandle(List<SelectorInterceptor> interceptors, SelectorBinding binding) {
        for (SelectorInterceptor interceptor : interceptors) {
            binding = interceptor.preHandle(binding);
        }

        return binding;
    }

    public SelectorResult postHandle(List<SelectorInterceptor> interceptors, SelectorBinding binding, SelectorResult value) {
        for (SelectorInterceptor interceptor : interceptors) {
            value = interceptor.postHandle(binding, value);
        }

        return value;
    }
}