package org.team4u.command.infrastructure.config;

import org.team4u.base.config.AbstractJsonCacheableConfigRepository;
import org.team4u.base.config.ConfigService;
import org.team4u.base.serializer.Serializer;
import org.team4u.base.serializer.json.JsonCacheableSerializers;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.config.CommandConfigRepository;

import java.util.Set;

/**
 * 基于json的命令配置
 *
 * @author jay.wu
 */
public class JsonCommandConfigRepository
        extends AbstractJsonCacheableConfigRepository<CommandConfig>
        implements CommandConfigRepository {

    public JsonCommandConfigRepository(ConfigService configService) {
        super(configService);
    }

    @Override
    public void save(CommandConfig config) {
        throw new UnsupportedOperationException("save");
    }

    @Override
    public Set<String> allConfigIdList() {
        return getConfigService().allConfigs().keySet();
    }

    @Override
    protected Serializer serializer() {
        return JsonCacheableSerializers.getInstance().serializer();
    }
}