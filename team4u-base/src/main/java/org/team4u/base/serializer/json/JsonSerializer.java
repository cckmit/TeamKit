package org.team4u.base.serializer.json;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.base.serializer.Serializer;

/**
 * json序列化器
 *
 * @author jay.wu
 */
public interface JsonSerializer extends Serializer {

    @Override
    default boolean supports(Object context) {
        if (context == null) {
            return false;
        }

        // 反序列化时判断是否为json
        if (context instanceof String) {
            return JSONUtil.isJson((String) context);
        }

        // 序列化时不处理简单类型
        return !ClassUtil.isSimpleValueType(context.getClass());
    }
}