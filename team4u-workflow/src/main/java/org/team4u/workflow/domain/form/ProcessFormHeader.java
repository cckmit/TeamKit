package org.team4u.workflow.domain.form;

/**
 * 流程实例概览
 *
 * @author jay.wu
 */
public interface ProcessFormHeader {

    /**
     * 获取自增长标识
     *
     * @return 自增长标识
     */
    Long getId();

    /**
     * 获取流程实例标识
     *
     * @return 流程实例标识
     */
    String getProcessInstanceId();
}