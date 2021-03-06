package org.team4u.workflow.application.command;

import java.util.Date;

public class InstancesQuery {
    private String processInstanceId;
    private String processInstanceType;
    private String processInstanceName;
    private String processInstanceNodeId;
    private String operator;
    private Date createStartTime;
    private Date createEndTime;
    /**
     * 页码，0表示第一页
     */
    private int pageNumber = 0;
    private int pageSize = 10;
    /**
     * 是否加载detail数据
     */
    private boolean withDetail = false;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public InstancesQuery setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public String getProcessInstanceType() {
        return processInstanceType;
    }

    public InstancesQuery setProcessInstanceType(String processInstanceType) {
        this.processInstanceType = processInstanceType;
        return this;
    }

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public InstancesQuery setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
        return this;
    }

    public String getProcessInstanceNodeId() {
        return processInstanceNodeId;
    }

    public InstancesQuery setProcessInstanceNodeId(String processInstanceNodeId) {
        this.processInstanceNodeId = processInstanceNodeId;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public InstancesQuery setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public InstancesQuery setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
        return this;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public InstancesQuery setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
        return this;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public InstancesQuery setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public InstancesQuery setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public boolean isWithDetail() {
        return withDetail;
    }

    public InstancesQuery setWithDetail(boolean withDetail) {
        this.withDetail = withDetail;
        return this;
    }
}
