package org.team4u.workflow.infrastructure.persistence.definition;

import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import org.team4u.base.serializer.CacheableJsonSerializer;
import org.team4u.base.serializer.Serializer;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;

import java.lang.reflect.Type;

/**
 * 流程定义工具类
 *
 * @author jay.wu
 */
public class ProcessDefinitionUtil {

    private static final CacheableJsonSerializer cacheSerializer = new CacheableJsonSerializer(
            new Serializer() {
                @Override
                public String serialize(Object value) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public <T> T deserialize(String serialization, Class<T> type) {
                    return JSON.parseObject(
                            serialization, type, Feature.SupportAutoType
                    );
                }

                @Override
                public <T> T deserialize(String serialization, Type type) {
                    throw new UnsupportedOperationException();
                }
            }, CacheUtil.newLRUCache(1000)
    );

    /**
     * 根据Json生成流程定义
     *
     * @param processDefinitionId 流程定义标识
     * @param json                流程定义Json
     * @return 流程定义
     */
    public static ProcessDefinition definitionOfJson(ProcessDefinitionId processDefinitionId,
                                                     String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }

        ProcessDefinition definition = cacheSerializer.deserialize(json, ProcessDefinition.class);
        ReflectUtil.setFieldValue(definition, "processDefinitionId", processDefinitionId);
        return definition;
    }
}