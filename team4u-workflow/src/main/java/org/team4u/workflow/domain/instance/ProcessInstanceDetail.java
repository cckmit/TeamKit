package org.team4u.workflow.domain.instance;

import com.alibaba.fastjson.JSON;
import org.team4u.base.util.ConvertUtil;
import org.team4u.ddd.domain.model.IdentifiedValueObject;

/**
 * 流程实例明细
 *
 * @author jay.wu
 */
public class ProcessInstanceDetail extends IdentifiedValueObject {
    /**
     * 流程实例明细内容
     */
    private String body;

    public ProcessInstanceDetail(Object body) {
        setBodyObject(body);
    }

    public void setBodyObject(Object formBody) {
        if (formBody == null) {
            return;
        }

        if (formBody instanceof String) {
            setBody((String) formBody);
            return;
        }

        setBody(JSON.toJSONString(formBody));
    }

    @SuppressWarnings("unchecked")
    public <T> T toDetailObject(Class<?> type) {
        return (T) ConvertUtil.convert(getBody(), type);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}