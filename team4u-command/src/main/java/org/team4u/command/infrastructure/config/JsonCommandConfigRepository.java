package org.team4u.command.infrastructure.config;

import org.team4u.base.config.AbstractJsonConfigRepository;
import org.team4u.base.config.ConfigService;
import org.team4u.base.serializer.FastJsonCacheSerializer;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.config.CommandConfigRepository;

/**
 * 基于json的命令配置
 *
 * @author jay.wu
 */
public class JsonCommandConfigRepository
        extends AbstractJsonConfigRepository<CommandConfig>
        implements CommandConfigRepository {

    public JsonCommandConfigRepository(ConfigService configService) {
        super(configService);
    }

    @Override
    public void save(CommandConfig config) {
        throw new UnsupportedOperationException("save");
    }

    @Override
    protected CommandConfig deserialize(String configString, Class<CommandConfig> configClass) {
        return FastJsonCacheSerializer.instance().deserialize(configString, configClass);
    }
}