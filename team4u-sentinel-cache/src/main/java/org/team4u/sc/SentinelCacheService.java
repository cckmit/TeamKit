package org.team4u.sc;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessageContext;
import org.team4u.kv.KeyValueCleaner;
import org.team4u.kv.KeyValueRepository;
import org.team4u.kv.KeyValueService;
import org.team4u.kv.LockService;

import java.util.concurrent.TimeoutException;

/**
 * 基于哨兵模式的缓存服务
 * <p>
 * 第一批请求中，从缓存中获取结果，若结果缓存不存在，选取最早的那个请求为哨兵，由这个线程去计算结果并更新缓存。
 * 其他线程则自旋+sleep等待，直到哨兵更新缓存后，能拿到数据为止。
 *
 * @author jay.wu
 */
public class SentinelCacheService {

    private final static String LOCK_TYPE = "SC_LOCK";
    private final static String RESULT_TYPE = "SC_RESULT";
    private final Log log = LogFactory.get();
    private LockService lockService;
    private KeyValueService resultCacheService;

    public SentinelCacheService(KeyValueRepository keyValueRepository,
                                KeyValueCleaner keyValueCleaner) {
        this.resultCacheService = new KeyValueService(keyValueRepository, keyValueCleaner);
        this.lockService = new LockService(resultCacheService);
    }

    public Object invoke(InvokeCommand command) throws Throwable {
        LogMessageContext.createAndSet(this.getClass().getSimpleName(), "invoke")
                .append("id", command.getId())
                .append("resultClass", command.getResultClass().getSimpleName())
                .append("ttlMillis", command.getResultTtlMillis())
                .append("timeoutMillis", command.getLockTtlMillis());

        // 获取结果缓存
        Object result = resultOfCache(command.getId(), command.getResultClass());
        if (result != null) {
            return result;
        }

        boolean lock = false;
        try {
            lock = lockService.tryLock(LOCK_TYPE, command.getId(), command.getLockTtlMillis());
        } catch (Exception e) {
            log.error(e, LogMessageContext.get().fail("tryLock fail").toString());
        }

        if (lock) {
            // 获得锁，成为哨兵，执行方法
            return resultOfLocker(command);
        }

        // 获取独占锁失败，自旋等待缓存结果
        return waitingResultOfCache(command);
    }

    /**
     * 从缓存获取结果
     */
    private Object resultOfCache(String id, Class<?> resultClass) {
        Object result = resultCacheService.get(RESULT_TYPE, id, resultClass);
        if (result != null) {
            // 处于防刷时间窗口，使用结果缓存
            log.info(LogMessageContext.get()
                    .success()
                    .append("resultOfCache", true)
                    .toString());
            return result;
        }

        return null;
    }

    /**
     * 从哨兵获取结果
     */
    private Object resultOfLocker(InvokeCommand command) throws Throwable {
        Object result;
        try {
            result = command.getInvoker().invoke();
        } catch (Throwable e) {
            // 系统异常，尝试删除独占锁
            lockService.unLock(LOCK_TYPE, command.getId());
            throw e;
        }

        try {
            // 系统异常，不缓存结果
            if (command.getSystemErrorDecider().isSystemError(
                    result,
                    command.getResultCodeKey(),
                    command.getSystemErrorCodes()
            )) {
                return result;
            }

            // 处理完成后设置结果缓存
            resultCacheService.put(RESULT_TYPE, command.getId(), result, command.getResultTtlMillis());
        } catch (Exception e) {
            // 内部异常不能影响正常业务，仅打印日志
            log.error(e, LogMessageContext.get()
                    .fail("resultOfLocker error")
                    .toString());
        } finally {
            // 处理完成，删除独占锁
            lockService.unLock(LOCK_TYPE, command.getId());
        }

        return result;
    }

    /**
     * 等待缓存结果
     */
    private Object waitingResultOfCache(InvokeCommand command) throws TimeoutException {
        try {
            Object result = resultCacheService.get(
                    RESULT_TYPE,
                    command.getId(),
                    command.getResultClass(),
                    500,
                    command.getLockTtlMillis()
            );

            log.info(LogMessageContext.get()
                    .success()
                    .append("waitingResultOfCache", true)
                    .toString());

            return result;
        } catch (TimeoutException e) {
            log.error(LogMessageContext.get()
                    .fail("waitingResultOfCache timeout")
                    .toString());

            // 超过等待时间，确保删除独占锁
            lockService.unLock(LOCK_TYPE, command.getId());
            throw e;
        } catch (Exception e) {
            log.error(e, LogMessageContext.get()
                    .fail("waitingResultOfCache error")
                    .toString());

            return null;
        }
    }
}