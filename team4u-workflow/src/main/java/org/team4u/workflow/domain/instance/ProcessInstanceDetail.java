package org.team4u.workflow.domain.instance;

import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.team4u.ddd.domain.model.IdentifiedValueObject;

import static com.alibaba.fastjson.parser.Feature.SupportAutoType;

/**
 * 流程实例明细
 *
 * @author jay.wu
 */
public class ProcessInstanceDetail extends IdentifiedValueObject {
    /**
     * 流程实例明细类型
     */
    private String type;
    /**
     * 流程实例明细内容
     */
    private String body;

    public ProcessInstanceDetail(String type, String body) {
        this.type = type;
        this.body = body;
    }

    public ProcessInstanceDetail(Object body) {
        setBodyAndType(body);
    }

    public void setBodyAndType(Object formBody) {
        if (formBody == null) {
            return;
        }

        setBody(JSON.toJSONString(formBody, SerializerFeature.WriteClassName));
        setType(formBody.getClass().getName());
    }

    public <T> T toDetailObject() {
        Class<T> bodyClass = ClassUtil.loadClass(getType());
        return JSON.parseObject(getBody(), bodyClass, SupportAutoType);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
