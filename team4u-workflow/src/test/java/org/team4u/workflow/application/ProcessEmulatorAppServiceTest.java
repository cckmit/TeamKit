package org.team4u.workflow.application;

import org.junit.Test;
import org.team4u.base.config.ConfigService;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.ddd.infrastructure.persistence.memory.LogOnlyEventStore;
import org.team4u.workflow.domain.form.DefaultProcessFormPermissionService;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.domain.instance.node.handler.DynamicChoiceNodeHandler;
import org.team4u.workflow.infrastructure.persistence.definition.JsonProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.emulator.JsonProcessEmulatorScriptRepository;
import org.team4u.workflow.infrastructure.persistence.form.InMemoryProcessFormRepository;
import org.team4u.workflow.infrastructure.persistence.form.TestForm;
import org.team4u.workflow.infrastructure.persistence.instance.InMemoryProcessInstanceRepository;

import static org.team4u.workflow.TestUtil.TEST;
import static org.team4u.workflow.TestUtil.selectorAppService;

public class ProcessEmulatorAppServiceTest {

    private final ProcessEmulatorAppService emulatorAppService = emulatorAppService();

    @Test
    public void simulate() {
        emulatorAppService.simulate(
                "test_simple_completed_script",
                TestForm.Builder
                        .newBuilder()
                        .withFormId(TEST)
                        .build(),
                null
        );
    }

    private ProcessEmulatorAppService emulatorAppService() {
        LogOnlyEventStore eventStore = new LogOnlyEventStore();
        ConfigService configService = new LocalJsonConfigService();
        ProcessNodeHandlers handlers = new ProcessNodeHandlers();

        handlers.saveIdObject(new DynamicChoiceNodeHandler(selectorAppService()));

        return new ProcessEmulatorAppService(
                new ProcessFormAppService(
                        new ProcessAppService(
                                eventStore,
                                handlers,
                                new InMemoryProcessInstanceRepository(eventStore),
                                new JsonProcessDefinitionRepository(configService)),
                        new InMemoryProcessFormRepository<TestForm>(),
                        new DefaultProcessFormPermissionService()
                ),
                new JsonProcessEmulatorScriptRepository(configService)
        );
    }
}