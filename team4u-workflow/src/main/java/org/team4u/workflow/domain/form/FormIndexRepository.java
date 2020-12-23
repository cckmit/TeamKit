package org.team4u.workflow.domain.form;

/**
 * 表单索引资源库
 *
 * @author jay.wu
 */
public interface FormIndexRepository<F extends FormIndex> {

    /**
     * 查询表单索引
     *
     * @param instanceId 流程实例标识
     * @return 表单索引
     */
    F formOf(String instanceId);

    /**
     * 保存表单索引
     *
     * @param formIndex 表单索引
     */
    void save(F formIndex);
}