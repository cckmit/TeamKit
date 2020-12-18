package org.team4u.workflow.application;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScript;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptRepository;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptStep;
import org.team4u.workflow.domain.instance.ProcessInstance;

import java.util.Map;


/**
 * 流程模拟器应用服务
 *
 * @author jay.wu
 */
public class ProcessEmulatorAppService {

    private final ProcessAppService processAppService;
    private final ProcessEmulatorScriptRepository processEmulatorScriptRepository;

    public ProcessEmulatorAppService(ProcessAppService processAppService,
                                     ProcessEmulatorScriptRepository processEmulatorScriptRepository) {
        this.processAppService = processAppService;
        this.processEmulatorScriptRepository = processEmulatorScriptRepository;
    }

    /**
     * 开始模拟
     */
    public void simulate(String scriptId, Map<String, Object> ext) {
        ProcessEmulatorScript script = processEmulatorScriptRepository.scriptOf(scriptId);
        String processInstanceId = null;

        for (ProcessEmulatorScriptStep step : script.getSteps()) {
            initExt(step, ext);
            ProcessInstance instance = doStep(
                    processInstanceId,
                    script.getProcessDefinitionId(),
                    step
            );

            processInstanceId = instance.getProcessInstanceId();
        }
    }

    private void initExt(ProcessEmulatorScriptStep step, Map<String, Object> ext) {
        Map<String, Object> newExt = MapUtil.newHashMap();

        if (ext != null) {
            newExt.putAll(ext);
        }

        if (step.getExt() != null) {
            newExt.putAll(step.getExt());
        }

        step.setExt(newExt);
    }

    private ProcessInstance doStep(String processInstanceId,
                                   String processDefinitionId,
                                   ProcessEmulatorScriptStep step) {
        ProcessInstance instance = processAppService.start(new StartProcessInstanceCommand()
                .setActionId(step.getActionId())
                .setProcessInstanceId(processInstanceId)
                .setExt(step.getExt())
                .setOperatorId(step.getOperatorId())
                .setProcessDefinitionId(processDefinitionId)
        );

        checkStepExpected(step, instance);

        return instance;
    }

    private void checkStepExpected(ProcessEmulatorScriptStep step, ProcessInstance instance) {
        if (step.getExpected() == null) {
            return;
        }

        LogMessage lm = LogMessages.createWithMasker(getClass().getSimpleName(), "checkStepExpected")
                .append("step", step)
                .append("instance", instance);

        Assert.isTrue(
                StrUtil.equals(
                        step.getExpected().getNodeId(),
                        instance.getCurrentNode().getNodeId()),
                lm.fail("NodeId is not expected").toString()
        );
    }
}