package org.team4u.workflow.application.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.team4u.workflow.domain.form.FormIndex;

import java.util.Map;

/**
 * 开始表单索引命令
 *
 * @author jay.wu
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class StartProcessFormCommand extends AbstractHandleProcessInstanceCommand {
    /**
     * 流程动作标识
     */
    private String actionId;
    /**
     * 表单索引
     */
    private FormIndex formIndex;
    /**
     * 附加信息
     */
    private Map<String, Object> ext;
}