package org.team4u.base.config;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.serializer.CacheableSerializer;
import org.team4u.base.serializer.HutoolJsonSerializer;
import org.team4u.base.serializer.Serializer;

import java.util.Set;

/**
 * 基于json的抽象缓存配置资源库
 *
 * @author jay.wu
 */
public abstract class AbstractJsonCacheableConfigRepository<T> {

    private final ConfigService configService;

    private final CacheableSerializer cacheableJsonSerializer = new CacheableSerializer(
            serializer()
    );

    @SuppressWarnings("unchecked")
    private final Class<T> configClass = (Class<T>) ClassUtil.getTypeArgument(this.getClass());

    public AbstractJsonCacheableConfigRepository(ConfigService configService) {
        this.configService = configService;
    }

    public T configOfId(String configId) {
        T config = deserialize(configService.get(configId), configClass());

        if (config == null) {
            return null;
        }

        if (config instanceof IdentifiedConfig) {
            setConfigId((IdentifiedConfig) config, configId);
        }

        return config;
    }

    public Set<String> allConfigIdList() {
        return configService.allConfigs().keySet();
    }

    private void setConfigId(IdentifiedConfig config, String id) {
        if (StrUtil.isBlank(config.getConfigId())) {
            config.setConfigId(id);
        }
    }

    public Class<T> configClass() {
        return configClass;
    }

    protected T deserialize(String configString, Class<T> configClass) {
        return cacheableJsonSerializer.deserialize(configString, configClass);
    }

    protected Serializer serializer() {
        return HutoolJsonSerializer.instance();
    }

    public ConfigService getConfigService() {
        return configService;
    }
}