package org.team4u.kv;

/**
 * 简单锁服务
 * <p>
 * 相对于LockService，本类针对同一种类型提供更加便捷的接口
 *
 * @author jay.wu
 */
public class SimpleLockService {

    private String type;
    private LockService lockService;

    public SimpleLockService(String type, KeyValueService keyValueService) {
        this.type = type;
        this.lockService = new LockService(keyValueService);
    }

    /**
     * 尝试获取锁
     * <p>
     * 注意，需要考虑抛出异常，如果底层依赖第三方存储
     *
     * @param id 锁标识
     * @return 是否成功获取锁
     */
    public boolean tryLock(String id) {
        return lockService.tryLock(type, id);
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
    public boolean tryLock(String id, int ttlMillis) {
        return lockService.tryLock(type, id, ttlMillis);
    }

    /**
     * 解锁
     *
     * @param id 锁标识
     */
    public void unLock(String id) {
        lockService.unLock(type, id);
    }
}