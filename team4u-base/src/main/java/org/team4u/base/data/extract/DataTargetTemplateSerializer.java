package org.team4u.base.data.extract;

import java.util.List;

/**
 * 数据目标模板序列化器
 *
 * @author jay.wu
 */
public interface DataTargetTemplateSerializer {

    /**
     * 将对象序列化为可替换的中间值
     */
    Object serializeToReplaceableObject(String value);

    /**
     * 反序列化为目标值
     *
     * @param replaceableObject 中间值
     * @param type              目标类型
     */
    <T> T deserializeToBean(Object replaceableObject, Class<T> type);

    /**
     * 反序列化为集合
     *
     * @param replaceableObject 中间值
     * @param type              集合内的元素类型
     */
    <T> List<T> deserializeToList(Object replaceableObject, Class<T> type);
}