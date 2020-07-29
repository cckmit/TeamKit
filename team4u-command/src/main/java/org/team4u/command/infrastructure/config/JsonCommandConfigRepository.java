package org.team4u.command.infrastructure.config;

import com.alibaba.fastjson.JSON;
import org.team4u.base.config.ConfigService;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.config.CommandConfigRepository;

/**
 * 基于json的命令配置
 *
 * @author jay.wu
 */
public class JsonCommandConfigRepository implements CommandConfigRepository {

    private final ConfigService configService;

    public JsonCommandConfigRepository(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public CommandConfig configOf(String id) {
        String configStr = configService.get(id);
        return JSON.parseObject(configStr, CommandConfig.class);
    }

    @Override
    public void save(CommandConfig config) {
        throw new UnsupportedOperationException("save");
    }
}