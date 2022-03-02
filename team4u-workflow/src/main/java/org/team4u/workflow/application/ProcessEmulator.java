package org.team4u.workflow.application;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import org.team4u.base.config.ConfigService;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.infrastructure.persistence.memory.InMemoryEventStore;
import org.team4u.workflow.application.command.CreateProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessFormCommand;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScript;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptNotExistException;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptRepository;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptStep;
import org.team4u.workflow.domain.form.DefaultFormPermissionService;
import org.team4u.workflow.domain.form.FormIndex;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.infrastructure.persistence.definition.JsonProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.emulator.JsonProcessEmulatorScriptRepository;
import org.team4u.workflow.infrastructure.persistence.form.InMemoryFormIndexRepository;
import org.team4u.workflow.infrastructure.persistence.instance.InMemoryProcessInstanceRepository;

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
     * 创建模拟器
     */
    public static ProcessEmulator create() {
        EventStore eventStore = new InMemoryEventStore();
        ConfigService configService = new LocalJsonConfigService();

        return new ProcessEmulator(
                new ProcessFormAppService(
                        eventStore,
                        new ProcessAppService(
                                new InMemoryProcessInstanceRepository(eventStore),
                                new JsonProcessDefinitionRepository(configService)),
                        new InMemoryFormIndexRepository<>(),
                        new DefaultFormPermissionService()
                ),
                new JsonProcessEmulatorScriptRepository(configService)
        );
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

        ProcessEmulatorScript script = scriptOf(scriptId);

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

    private ProcessEmulatorScript scriptOf(String scriptId) {
        ProcessEmulatorScript script = processEmulatorScriptRepository.configOfId(scriptId);

        if (script == null) {
            throw new ProcessEmulatorScriptNotExistException(scriptId);
        }

        return script;
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
            CreateProcessFormCommand command = CreateProcessFormCommand.builder()
                    .operatorId(step.getOperatorId())
                    .formIndex(formIndex)
                    .processDefinitionId(processDefinitionId)
                    .build();
            processInstanceId = formAppService.create(command);
        }

        StartProcessFormCommand command = StartProcessFormCommand.builder()
                .ext(step.getExt())
                .operatorId(step.getOperatorId())
                .actionId(step.getActionId())
                .processInstanceId(processInstanceId)
                .formIndex(formIndex)
                .build();
        formAppService.start(command);

        return formAppService.getProcessAppService().availableProcessInstanceOf(processInstanceId);
    }

    private void checkStepExpected(ProcessEmulatorScriptStep step, ProcessInstance instance) {
        if (step.getExpected() == null) {
            return;
        }

        LogMessage lm = LogMessages.createWithMasker(getClass().getSimpleName(), "checkStepExpected")
                .append("step", step)
                .append("instance", instance);
        log.info(lm.success().toString());

        Assert.isTrue(
                StrUtil.equals(
                        step.getExpected().getNodeId(),
                        instance.getCurrentNode().getNodeId()),
                String.format("expected:%s,but:%s",
                        step.getExpected().getNodeId(),
                        instance.getCurrentNode().getNodeId())
        );
    }

    public ProcessFormAppService getFormAppService() {
        return formAppService;
    }
}