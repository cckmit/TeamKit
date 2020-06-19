package org.team4u.kv.resource;

import java.util.List;

/**
 * 存储资源服务
 *
 * @author jay.wu
 */
public interface StoreResourceService {

    /**
     * 选择资源
     */
    StoreResource select(String key);

    /**
     * 所有资源集合
     */
    List<StoreResource> resources();

    /**
     * 重新初始化资源
     */
    void reload();
}