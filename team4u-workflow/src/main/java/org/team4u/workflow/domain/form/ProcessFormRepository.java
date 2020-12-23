package org.team4u.workflow.domain.form;

/**
 * 流程表单资源库
 *
 * @author jay.wu
 */
public interface ProcessFormRepository<F extends ProcessForm> {

    /**
     * 查询流程表单
     *
     * @param instanceId 流程实例标识
     * @return 流程表单对象
     */
    F formOf(String instanceId);

    /**
     * 保存流程表单
     *
     * @param form 表单对象
     */
    void save(F form);
}