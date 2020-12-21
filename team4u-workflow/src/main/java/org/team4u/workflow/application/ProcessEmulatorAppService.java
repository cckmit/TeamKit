package org.team4u.workflow.application;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.workflow.application.command.AbstractHandleProcessInstanceCommand;
import org.team4u.workflow.application.command.CreateProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessFormCommand;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScript;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptRepository;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptStep;
import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.instance.ProcessInstance;

import java.util.Map;


/**
 * 流程模拟器应用服务
 *
 * @author jay.wu
 */
public class ProcessEmulatorAppService {

    private final ProcessFormAppService formAppService;
    private final ProcessEmulatorScriptRepository processEmulatorScriptRepository;

    public ProcessEmulatorAppService(ProcessFormAppService formAppService,
                                     ProcessEmulatorScriptRepository processEmulatorScriptRepository) {
        this.formAppService = formAppService;
        this.processEmulatorScriptRepository = processEmulatorScriptRepository;
    }

    /**
     * 开始模拟
     */
    public void simulate(String scriptId, ProcessForm form, Map<String, Object> ext) {
        ProcessEmulatorScript script = processEmulatorScriptRepository.scriptOf(scriptId);
        String processInstanceId = null;

        for (ProcessEmulatorScriptStep step : script.getSteps()) {
            initExt(step, ext);
            ProcessInstance instance = doStep(
                    processInstanceId,
                    script.getProcessDefinitionId(),
                    form,
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
                                   ProcessForm processForm,
                                   ProcessEmulatorScriptStep step) {
        if (processInstanceId == null) {
            CreateProcessFormCommand command = CreateProcessFormCommand.Builder
                    .newBuilder()
                    .withProcessForm(processForm)
                    .withProcessDefinitionId(processDefinitionId)
                    .build();
            initProcessInstanceCommand(command, step);
            formAppService.create(command);
        } else {
            StartProcessFormCommand command = StartProcessFormCommand.Builder
                    .newBuilder()
                    .withProcessForm(processForm)
                    .build();
            initProcessInstanceCommand(command, step);
            formAppService.start(command);
        }

        ProcessInstance instance = formAppService.formOf(
                processForm.getFormId(),
                step.getOperatorId()
        ).getInstance();

        checkStepExpected(step, instance);

        return instance;
    }

    private void initProcessInstanceCommand(AbstractHandleProcessInstanceCommand command,
                                            ProcessEmulatorScriptStep step) {
        command.setActionId(step.getActionId())
                .setExt(step.getExt())
                .setOperatorId(step.getOperatorId());
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