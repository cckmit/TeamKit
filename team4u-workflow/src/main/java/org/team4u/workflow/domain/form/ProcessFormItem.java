package org.team4u.workflow.domain.form;

import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSON;
import org.team4u.ddd.domain.model.IdentifiedValueObject;

import static com.alibaba.fastjson.parser.Feature.SupportAutoType;

/**
 * 流程表单内容
 *
 * @author jay.wu
 */
public class ProcessFormItem extends IdentifiedValueObject {
    /**
     * 流程表单标识
     */
    private String formId;
    /**
     * 表单内容类型
     */
    private String formBodyType;
    /**
     * 流程表单内容
     */
    private String formBody;

    public <T> T toFormBody() {
        Class<T> bodyClass = ClassUtil.loadClass(getFormBodyType());
        return JSON.parseObject(getFormBody(), bodyClass, SupportAutoType);
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormBodyType() {
        return formBodyType;
    }

    public void setFormBodyType(String formBodyType) {
        this.formBodyType = formBodyType;
    }

    public String getFormBody() {
        return formBody;
    }

    public void setFormBody(String formBody) {
        this.formBody = formBody;
    }
}
