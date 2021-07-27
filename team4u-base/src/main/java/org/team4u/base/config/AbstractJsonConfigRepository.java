package org.team4u.base.config;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.serializer.HutoolJsonCacheSerializer;

/**
 * 基于json的抽象配置资源库
 *
 * @author jay.wu
 */
public abstract class AbstractJsonConfigRepository<T> {

    private final ConfigService configService;

    @SuppressWarnings("unchecked")
    private final Class<T> configClass = (Class<T>) ClassUtil.getTypeArgument(this.getClass());

    public AbstractJsonConfigRepository(ConfigService configService) {
        this.configService = configService;
    }

    public T configOfId(String configId) {
        String configString = configService.get(configId);
        T config = deserialize(configString, configClass());

        if (config == null) {
            return null;
        }

        if (config instanceof IdentifiedConfig) {
            setConfigId((IdentifiedConfig) config, configId);
        }

        return config;
    }

    private void setConfigId(IdentifiedConfig config, String id) {
        if (StrUtil.isBlank(config.getId())) {
            config.setId(id);
        }
    }

    public Class<T> configClass() {
        return configClass;
    }

    protected T deserialize(String configString, Class<T> configClass) {
        return HutoolJsonCacheSerializer.instance().deserialize(configString, configClass);
    }
}