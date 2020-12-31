public enum ProcessNode {
    /**
     * 创建
     */
    created,
    /**
     * 内部处理
     */
    beanProcessing,
    /**
     * bea动作选择器1
     */
    beanChoiceNode,
    /**
     * bea动作选择器2
     */
    beanChoiceNode2,
    /**
     * 结束流程
     */
    simpleChoice,
    /**
     * 完成
     */
    completed,
    ;
}