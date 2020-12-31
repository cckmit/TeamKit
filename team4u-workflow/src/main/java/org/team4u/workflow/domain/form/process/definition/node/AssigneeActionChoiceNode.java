package org.team4u.workflow.domain.form.process.definition.node;

import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.definition.node.TransientNode;

import java.util.List;

/**
 * 处理人动作选择器节点
 *
 * @author jay.wu
 */
public class AssigneeActionChoiceNode extends ActionChoiceNode implements TransientNode {

    public static final String CHOICE_TYPE_ANY = "ANY";
    public static final String CHOICE_TYPE_ALL = "ALL";

    /**
     * 选择类型
     */
    private final String choiceType;
    /**
     * 遇到指定动作标识时立刻做出选择
     * <p>
     * 如：拒绝动作
     */
    private final String choiceActionId;

    public AssigneeActionChoiceNode(String nodeId,
                                    String nodeName,
                                    String choiceType,
                                    List<ActionNode> actionNodes) {
        this(nodeId, nodeName, choiceType, null, actionNodes);
    }

    public AssigneeActionChoiceNode(String nodeId,
                                    String nodeName,
                                    String choiceType,
                                    String choiceActionId,
                                    List<ActionNode> actionNodes) {
        super(nodeId, nodeName, actionNodes);
        this.choiceType = choiceType;
        this.choiceActionId = choiceActionId;
    }

    public String getChoiceType() {
        return choiceType;
    }

    public String getChoiceActionId() {
        return choiceActionId;
    }
}