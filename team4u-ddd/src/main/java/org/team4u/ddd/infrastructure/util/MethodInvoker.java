package org.team4u.ddd.infrastructure.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 方法执行器
 *
 * @author jay.wu
 */
public class MethodInvoker {

    /**
     * 执行方法
     *
     * @param obj                方法所在实例
     * @param methodName         方法名称
     * @param parameterTypes     方法类型数组
     * @param rawParameterValues json格式的方法入参值
     * @return 方法返回值
     * @throws Exception 执行异常
     */
    public Object invoke(Object obj,
                         String methodName,
                         Class<?>[] parameterTypes,
                         String rawParameterValues) throws Exception {
        return invoke(obj, methodName, parameterTypes, JSON.parseArray(rawParameterValues));
    }

    /**
     * 执行方法
     *
     * @param obj                方法所在实例
     * @param methodName         方法名称
     * @param parameterTypes     方法类型数组
     * @param rawParameterValues json格式的方法入参值
     * @return 方法返回值
     * @throws Exception 执行异常
     */
    public Object invoke(Object obj,
                         String methodName,
                         Class<?>[] parameterTypes,
                         JSONArray rawParameterValues) throws Exception {
        Method method = findMethod(obj, methodName, parameterTypes);
        Object[] args = toActualParameterValues(method, rawParameterValues);

        try {
            return ReflectUtil.invoke(obj, method, args);
        } catch (UtilException e) {
            throw findOriginalException(e);
        }
    }

    private Method findMethod(Object obj, String methodName, Class<?>[] parameterTypes) {
        return ClassUtil.getDeclaredMethod(
                AopUtils.getTargetClass(obj),
                methodName,
                parameterTypes
        );
    }

    private Object[] toActualParameterValues(Method method, JSONArray rawParameterValues) {
        Type[] types = method.getGenericParameterTypes();

        Object[] values = new Object[rawParameterValues.size()];

        for (int i = 0; i < types.length; i++) {
            Object rawValue = rawParameterValues.get(i);
            if (rawValue == null) {
                values[i] = null;
            }

            Type type = types[i];
            if (rawValue instanceof JSON) {
                values[i] = ((JSON) rawValue).toJavaObject(type);
            } else {
                values[i] = Convert.convert(type, rawValue);
            }
        }

        return values;
    }

    private Exception findOriginalException(UtilException e) {
        Throwable targetEx = e;

        Throwable cause = e.getCause();
        if (cause instanceof InvocationTargetException) {
            targetEx = ((InvocationTargetException) cause).getTargetException();
        }

        return (Exception) targetEx;
    }
}