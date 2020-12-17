package org.team4u.workflow.domain.form;

/**
 * 流程表单
 *
 * @author jay.wu
 */
public class ProcessForm {
    /**
     * 流程表单概览
     */
    private ProcessFormHeader formHeader;
    /**
     * 流程表单内容
     */
    private ProcessFormItem formItem;

    public ProcessFormHeader getFormHeader() {
        return formHeader;
    }

    public void setFormHeader(ProcessFormHeader formHeader) {
        this.formHeader = formHeader;
    }

    public ProcessFormItem getFormItem() {
        return formItem;
    }

    public void setFormItem(ProcessFormItem formItem) {
        this.formItem = formItem;
    }
}