package org.team4u.workflow.domain.form;

/**
 * 流程表单资源库
 *
 * @author jay.wu
 */
public interface ProcessFormRepository {

    /**
     * 查询流程表单明细
     *
     * @param formId 表单标识
     * @return 流程表单对象
     */
    ProcessForm formOf(String formId);

    /**
     * 保存流程表单
     *
     * @param form 表单对象
     */
    void save(ProcessForm form);
}