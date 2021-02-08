package org.team4u.ddd.process.retry;

import cn.hutool.core.util.ArrayUtil;
import org.team4u.ddd.process.TimeConstrainedProcessTracker;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;

import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * 重试服务
 *
 * @author jay.wu
 */
public class RetryService {

    private final Class<? extends Throwable>[] retryForClass;
    private final Class<? extends Throwable>[] noRetryForClass;

    private final TimeConstrainedProcessTrackerAppService trackerAppService;

    public RetryService(Class<? extends Throwable>[] retryForClass,
                        Class<? extends Throwable>[] noRetryForClass,
                        TimeConstrainedProcessTrackerAppService trackerAppService) {
        this.retryForClass = retryForClass;
        this.noRetryForClass = noRetryForClass;
        this.trackerAppService = trackerAppService;
    }

    /**
     * 判断异常是否需要重试
     *
     * @param throwable 待检查的异常
     * @return true:可重试,false:无需重试
     */
    public boolean retryOn(Throwable throwable) {
        if (throwable instanceof NeedRetryException) {
            return true;
        }

        if (ArrayUtil.isNotEmpty(retryForClass)) {
            if (Arrays.stream(retryForClass)
                    .anyMatch(it -> it.isAssignableFrom(throwable.getClass()))) {
                return true;
            }
        }

        if (ArrayUtil.isNotEmpty(noRetryForClass)) {
            if (Arrays.stream(noRetryForClass)
                    .anyMatch(it -> it.isAssignableFrom(throwable.getClass()))) {
                return false;
            }
        }

        // 未命中已定义的异常类，默认全部重试
        return true;
    }

    /**
     * 执行并且自动关闭跟踪器
     *
     * @param tracker                           跟踪器
     * @param shouldRemoveTrackerAfterCompleted 重试完成后是否需要清除跟踪器记录
     * @param callable                          执行体
     * @param <V>                               返回值类型
     * @return 执行结果
     * @throws Exception 执行异常
     */
    public <V> V invokeAndAutoCloseTracker(TimeConstrainedProcessTracker tracker,
                                           boolean shouldRemoveTrackerAfterCompleted,
                                           Callable<V> callable) throws Exception {
        try {
            V v = callable.call();

            trackerAppService.closeTracker(tracker, shouldRemoveTrackerAfterCompleted);

            return v;
        } catch (Exception e) {
            if (!retryOn(e)) {
                trackerAppService.closeTracker(tracker, shouldRemoveTrackerAfterCompleted);
            }

            if (e instanceof NeedRetryException) {
                return null;
            }

            throw e;
        }
    }

    /**
     * 初次执行，可自动关闭跟踪器
     *
     * @param tracker           跟踪器
     * @param shouldSaveTracker 是否保存跟踪器
     * @param callable          执行体
     * @param <V>               返回值类型
     * @return 执行结果
     * @throws Exception 执行异常
     */
    public <V> V invokeForFirstRun(TimeConstrainedProcessTracker tracker,
                                   boolean shouldSaveTracker,
                                   Callable<V> callable) throws Exception {
        if (shouldSaveTracker) {
            trackerAppService.saveTracker(tracker);
        }

        return invokeAndAutoCloseTracker(tracker, true, callable);
    }
}