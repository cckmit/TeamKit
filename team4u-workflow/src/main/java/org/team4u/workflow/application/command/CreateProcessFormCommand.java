package org.team4u.workflow.application.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.team4u.workflow.domain.form.FormIndex;

/**
 * 创建表单索引命令
 *
 * @author jay.wu
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CreateProcessFormCommand extends CreateProcessInstanceCommand {

    private FormIndex formIndex;
}