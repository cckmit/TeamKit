package org.team4u.workflow.infrastructure.emulator;

import com.alibaba.fastjson.JSON;
import org.team4u.base.config.ConfigService;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScript;
import org.team4u.workflow.domain.emulator.ProcessEmulatorScriptRepository;

/**
 * 基于Json的流程模拟器脚本资源库
 *
 * @author jay.wu
 */
public class JsonProcessEmulatorScriptRepository implements ProcessEmulatorScriptRepository {

    private final ConfigService configService;

    public JsonProcessEmulatorScriptRepository(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public ProcessEmulatorScript scriptOf(String scriptId) {
        String json = configService.get(scriptId);
        return JSON.parseObject(json, ProcessEmulatorScript.class);
    }
}