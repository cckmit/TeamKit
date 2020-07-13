package org.team4u.base.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


/**
 * 转换工具类
 *
 * @author Jay Wu
 */
public class ConvertUtil {

    /**
     * 转换值为指定类型，简单类型采用Convert，复杂类型采用JSON
     */
    public static <T> T convert(String body, Class<T> clazz) {
        if (body == null) {
            return null;
        }

        return ClassUtil.isSimpleValueType(clazz) ? Convert.convert(clazz, body) : JSON.parseObject(body, clazz);
    }

    /**
     * 转换值为指定类型
     */
    public static <T> T convert(String body, TypeReference<T> reference) {
        if (body == null) {
            return null;
        }

        return JSON.parseObject(body, reference);
    }

    /**
     * 转换为字符串，简单类型采用Convert，复杂类型采用JSON<br>
     * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static String toString(Object value) {
        if (value == null) {
            return null;
        }

        return ClassUtil.isSimpleValueType(value.getClass()) ? Convert.toStr(value) : JSON.toJSONString(value);
    }

    /**
     * 转换为Byte数组
     *
     * @param value 被转换的值
     * @return Byte数组
     */
    public static Byte[] toByteArray(Object value) {
        return Convert.toByteArray(toString(value));
    }
}