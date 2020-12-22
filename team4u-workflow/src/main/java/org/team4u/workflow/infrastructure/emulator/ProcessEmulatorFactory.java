package org.team4u.workflow.infrastructure.emulator;

import org.team4u.base.config.ConfigService;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.infrastructure.persistence.memory.InMemoryEventStore;
import org.team4u.workflow.application.ProcessAppService;
import org.team4u.workflow.application.ProcessEmulator;
import org.team4u.workflow.application.ProcessFormAppService;
import org.team4u.workflow.domain.form.DefaultProcessFormPermissionService;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.infrastructure.persistence.definition.JsonProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.emulator.JsonProcessEmulatorScriptRepository;
import org.team4u.workflow.infrastructure.persistence.form.InMemoryProcessFormRepository;
import org.team4u.workflow.infrastructure.persistence.instance.InMemoryProcessInstanceRepository;

/**
 * 流程模拟器工厂
 *
 * @author jay.wu
 */
public class ProcessEmulatorFactory {

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
                                new ProcessNodeHandlers(),
                                new InMemoryProcessInstanceRepository(eventStore),
                                new JsonProcessDefinitionRepository(configService)),
                        new InMemoryProcessFormRepository<>(),
                        new DefaultProcessFormPermissionService()
                ),
                new JsonProcessEmulatorScriptRepository(configService)
        );
    }
}