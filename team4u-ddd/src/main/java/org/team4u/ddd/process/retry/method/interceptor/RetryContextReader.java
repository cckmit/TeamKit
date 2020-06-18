package org.team4u.ddd.process.retry.method.interceptor;

import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 重试上下文阅读器
 *
 * @author jay.wu
 */
public class RetryContextReader {

    private final JSONObject context;

    public RetryContextReader(JSONObject context) {
        this.context = context;
    }

    public boolean shouldRemoveTrackerAfterCompleted() {
        return context.getBooleanValue("shouldRemoveTrackerAfterCompleted");
    }

    public String methodName() {
        return context.getString("methodName");
    }

    public String invokerId() {
        return context.getString("invokerId");
    }

    public JSONArray parameterValues() {
        return context.getJSONArray("parameterValues");
    }

    public Class<?>[] parameterTypes() {
        return toClasses(context, "parameterTypes");
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Throwable>[] retryForClass() {
        return (Class<? extends Throwable>[]) toClasses(context, "retryForClass");
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Throwable>[] noRetryForClass() {
        return (Class<? extends Throwable>[]) toClasses(context, "noRetryForClass");
    }

    private Class<?>[] toClasses(JSONObject context, String key) {
        List<Class<?>> retryForClass = context.getJSONArray(key)
                .stream()
                .map(it -> ClassUtil.loadClass(it.toString()))
                .collect(Collectors.toList());

        Class<?>[] classes = new Class[retryForClass.size()];
        return retryForClass.toArray(classes);
    }
}
