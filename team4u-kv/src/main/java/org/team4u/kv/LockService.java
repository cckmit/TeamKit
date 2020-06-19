package org.team4u.kv;

import cn.hutool.core.util.StrUtil;

/**
 * 锁服务
 *
 * @author jay.wu
 */
public class LockService {

    private KeyValueService keyValueService;

    public LockService(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    /**
     * 尝试获取锁
     * <p>
     * 注意，需要考虑抛出异常，如果底层依赖第三方存储
     *
     * @param id 锁标识
     * @return 是否成功获取锁
     */
    public boolean tryLock(String type, String id) {
        return tryLock(type, id, 0);
    }

    /**
     * 尝试获取锁
     * <p>
     * 注意，需要考虑抛出异常，如果底层依赖第三方存储
     *
     * @param id        锁标识
     * @param ttlMillis 锁有效时长（毫秒），超过有效期则自动释放锁
     * @return 是否成功获取锁
     */
    public boolean tryLock(String type, String id, int ttlMillis) {
        return StrUtil.isNotEmpty(keyValueService.putIfAbsent(type, id, 1, ttlMillis));
    }

    /**
     * 解锁
     *
     * @param id 锁标识
     */
    public void unLock(String type, String id) {
        keyValueService.remove(type, id);
    }
}