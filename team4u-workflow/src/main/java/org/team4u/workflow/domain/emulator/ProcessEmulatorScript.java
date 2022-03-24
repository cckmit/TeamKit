package org.team4u.workflow.domain.emulator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.base.config.IdentifiedConfig;

import java.util.List;

/**
 * 流程模拟器脚本
 *
 * @author jay.wu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessEmulatorScript extends IdentifiedConfig {

    private String processDefinitionId;

    private List<ProcessEmulatorScriptStep> steps;
}