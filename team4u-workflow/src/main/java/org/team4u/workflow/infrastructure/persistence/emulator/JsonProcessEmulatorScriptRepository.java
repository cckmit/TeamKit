package org.team4u.workflow.infrastructure.persistence.emulator;

import org.team4u.base.config.AbstractJsonConfigRepository;
import org.team4u.base.config.ConfigService;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScript;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptRepository;

/**
 * 基于Json的流程模拟器脚本资源库
 *
 * @author jay.wu
 */
public class JsonProcessEmulatorScriptRepository
        extends AbstractJsonConfigRepository<ProcessEmulatorScript>
        implements ProcessEmulatorScriptRepository {

    public JsonProcessEmulatorScriptRepository(ConfigService configService) {
        super(configService);
    }
}