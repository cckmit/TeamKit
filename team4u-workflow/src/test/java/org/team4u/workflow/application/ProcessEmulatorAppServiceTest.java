package org.team4u.workflow.application;

import org.junit.Test;
import org.team4u.base.config.ConfigService;
import org.team4u.ddd.infrastructure.persistence.memory.LogOnlyEventStore;
import org.team4u.workflow.domain.instance.DefaultProcessPermissionService;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.domain.instance.node.handler.DynamicChoiceNodeHandler;
import org.team4u.workflow.infrastructure.persistence.LocalConfigService;
import org.team4u.workflow.infrastructure.persistence.definition.JsonProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.emulator.JsonProcessEmulatorScriptRepository;
import org.team4u.workflow.infrastructure.persistence.instance.InMemoryProcessInstanceRepository;

import static org.team4u.workflow.TestUtil.selectorAppService;

public class ProcessEmulatorAppServiceTest {

    private final ProcessEmulatorAppService emulatorAppService = emulatorAppService();

    @Test
    public void simulate() {
        emulatorAppService.simulate("test_simple_completed_script", null);
    }

    private ProcessEmulatorAppService emulatorAppService() {
        ProcessNodeHandlers handlers = new ProcessNodeHandlers();
        handlers.saveIdObject(new DynamicChoiceNodeHandler(selectorAppService()));

        ConfigService configService = new LocalConfigService();

        return new ProcessEmulatorAppService(
                new ProcessAppService(
                        new LogOnlyEventStore(),
                        handlers,
                        new DefaultProcessPermissionService(),
                        new InMemoryProcessInstanceRepository(),
                        new JsonProcessDefinitionRepository(configService)
                ),
                new JsonProcessEmulatorScriptRepository(configService)
        );
    }
}