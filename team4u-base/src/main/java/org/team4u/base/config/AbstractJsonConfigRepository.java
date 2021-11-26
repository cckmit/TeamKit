package org.team4u.base.config;

import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.serializer.CacheableSerializer;
import org.team4u.base.serializer.HutoolJsonSerializer;
import org.team4u.base.serializer.Serializer;

import java.util.Set;

/**
 * 基于json的抽象配置资源库
 *
 * @author jay.wu
 */
public abstract class AbstractJsonConfigRepository<T> {

    private final ConfigService configService;

    private final CacheableSerializer cacheableJsonSerializer = new CacheableSerializer(
            serializer(), CacheUtil.newLRUCache(1000)
    );

    @SuppressWarnings("unchecked")
    private final Class<T> configClass = (Class<T>) ClassUtil.getTypeArgument(this.getClass());

    public AbstractJsonConfigRepository(ConfigService configService) {
        this.configService = configService;
    }

    public T configOfId(String configId) {
        String configString = configService.get(configId);

        if (StrUtil.isBlank(configString)) {
            return null;
        }

        T config = deserialize(configString, configClass());

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