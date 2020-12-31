package org.team4u.workflow.application;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.workflow.application.command.AbstractHandleProcessInstanceCommand;
import org.team4u.workflow.application.command.CreateProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessFormCommand;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScript;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptNotExistException;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptRepository;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptStep;
import org.team4u.workflow.domain.form.FormIndex;
import org.team4u.workflow.domain.instance.ProcessInstance;

import java.util.Map;


/**
 * 流程模拟器
 *
 * @author jay.wu
 */
public class ProcessEmulator {

    private final Log log = Log.get();

    private final ProcessFormAppService formAppService;
    private final ProcessEmulatorScriptRepository processEmulatorScriptRepository;

    public ProcessEmulator(ProcessFormAppService formAppService,
                           ProcessEmulatorScriptRepository processEmulatorScriptRepository) {
        this.formAppService = formAppService;
        this.processEmulatorScriptRepository = processEmulatorScriptRepository;
    }

    /**
     * 开始模拟
     */
    public void start(String scriptId) {
        start(scriptId, null, null);
    }

    /**
     * 开始模拟
     */
    public void start(String scriptId, Map<String, Object> ext) {
        start(scriptId, null, ext);
    }

    /**
     * 开始模拟
     */
    public void start(String scriptId, FormIndex form, Map<String, Object> ext) {
        LogMessage lm = LogMessages.create(this.getClass().getSimpleName(), "simulate")
                .append("scriptId", scriptId);
        log.info(lm.processing().toString());

        ProcessEmulatorScript script = processEmulatorScriptRepository.scriptOf(scriptId);
        if (script == null) {
            throw new ProcessEmulatorScriptNotExistException(scriptId);
        }

        String processInstanceId = null;

        for (ProcessEmulatorScriptStep step : script.getSteps()) {
            initExt(step, ext);

            ProcessInstance instance = doStep(
                    processInstanceId,
                    script.getProcessDefinitionId(),
                    ObjectUtil.defaultIfNull(form, new FormIndex()),
                    step
            );

            checkStepExpected(step, instance);

            processInstanceId = instance.getProcessInstanceId();
        }

        log.info(lm.success().toString());
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
                                   FormIndex formIndex,
                                   ProcessEmulatorScriptStep step) {
        if (processInstanceId == null) {
            CreateProcessFormCommand command = CreateProcessFormCommand.Builder
                    .create()
                    .withFormIndex(formIndex)
                    .withProcessDefinitionId(processDefinitionId)
                    .build();
            initProcessInstanceCommand(command, step);
            processInstanceId = formAppService.create(command);
        } else {
            StartProcessFormCommand command = StartProcessFormCommand.Builder
                    .create()
                    .withFormIndex(formIndex.setProcessInstanceId(processInstanceId))
                    .build();
            initProcessInstanceCommand(command, step);
            formAppService.start(command);
        }

        return formAppService.getProcessAppService().availableProcessInstanceOf(processInstanceId);
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

    public ProcessFormAppService getFormAppService() {
        return formAppService;
    }
}