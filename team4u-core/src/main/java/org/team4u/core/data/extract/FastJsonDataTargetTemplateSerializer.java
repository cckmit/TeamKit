package org.team4u.core.data.extract;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 基于FastJson的数据目标模板序列化器
 *
 * @author jay.wu
 */
public class FastJsonDataTargetTemplateSerializer implements DataTargetTemplateSerializer {

    @Override
    public Object serializeToReplaceableObject(String value) {
        return JSON.parse(value);
    }

    @Override
    public <T> T deserializeToBean(Object replaceableObject, Class<T> type) {
        if (replaceableObject instanceof JSONObject) {
            return ((JSONObject) replaceableObject).toJavaObject(type);
        }

        return null;
    }

    @Override
    public <T> List<T> deserializeToList(Object replaceableObject, Class<T> type) {
        if (replaceableObject instanceof JSONArray) {
            return ((JSONArray) replaceableObject).toJavaList(type);
        }

        return null;
    }
}