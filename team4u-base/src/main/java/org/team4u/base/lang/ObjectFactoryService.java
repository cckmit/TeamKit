package org.team4u.base.lang;

import java.util.List;

/**
 * 对象工厂服务
 *
 * @author jay.wu
 */
public class ObjectFactoryService<O> extends IdObjectService<String, ObjectFactory<O, ?>> {

    public ObjectFactoryService() {
    }

    public ObjectFactoryService(List<ObjectFactory<O, ?>> objects) {
        super(objects);
    }

    /**
     * 创建对象
     *
     * @param factoryId 工厂标识
     * @param config    配置内容
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public O create(String factoryId, String config) {
        StringConfigObjectFactory<O> factory = (StringConfigObjectFactory<O>) objectOfId(factoryId);

        if (factory == null) {
            return null;
        }

        return factory.create(config);
    }
}