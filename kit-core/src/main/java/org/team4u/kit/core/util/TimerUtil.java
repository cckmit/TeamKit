package org.team4u.kit.core.util;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimerUtil {

    private static final Log log = LogFactory.get();
    private static final List<Timer> timerList = new ArrayList<Timer>();
    private static ScheduledThreadPoolExecutor taskScheduler = new ScheduledThreadPoolExecutor(getBestPoolSize());

    /**
     * 立即启动，并以固定的频率来运行任务。后续任务的启动时间不受前次任务延时影响。
     *
     * @param task          具体待执行的任务
     * @param periodSeconds 每次执行任务的间隔时间(单位秒)
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(long periodSeconds, Runnable task) {
        return scheduleAtFixedRate(TimeUnit.SECONDS, 0, periodSeconds, task);
    }

    /**
     * 在指定的延时之后开始以固定的频率来运行任务。后续任务的启动时间不受前次任务延时影响。
     *
     * @param task         具体待执行的任务
     * @param initialDelay 首次执行任务的延时时间
     * @param period       每次执行任务的间隔时间
     * @param unit         时间单位
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(TimeUnit unit, long initialDelay,
                                                         long period, Runnable task) {
        return taskScheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    /**
     * 在指定的时间点开始以固定的频率运行任务。后续任务的启动时间不受前次任务延时影响。
     *
     * @param unit      时间单位
     * @param startTime 首次运行的时间点,支持 "yyyy-MM-dd HH:mm:ss" 格式
     * @param period    每次执行任务的间隔时间
     * @param task      具体待执行的任务
     */
    public static void scheduleAtFixedRate(TimeUnit unit, String startTime, long period, Runnable task) {
        Date dt = DateUtil.parse(startTime);
        scheduleAtFixedRate(unit, dt, period, task);
    }

    /**
     * 在指定的时间点开始以固定的频率运行任务。后续任务的启动时间不受前次任务延时影响。
     *
     * @param unit      时间单位
     * @param startTime 首次运行的时间点
     * @param period    每次执行任务的间隔时间
     * @param task      具体待执行的任务
     */
    public static void scheduleAtFixedRate(final TimeUnit unit, Date startTime,
                                           final long period, final Runnable task) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                taskScheduler.scheduleAtFixedRate(task, 0, period, unit);
                timer.cancel();
                timerList.remove(timer);
            }
        }, startTime);
        timerList.add(timer);
    }

    /**
     * 立即启动，两次任务间保持固定的时间间隔
     *
     * @param periodSeconds 两次任务的间隔时间(单位秒)
     * @param task          具体待执行的任务
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(long periodSeconds, Runnable task) {
        return scheduleWithFixedDelay(TimeUnit.SECONDS, 0, periodSeconds, task);
    }

    /**
     * 在指定的延时之后启动，两次任务间保持固定的时间间隔
     *
     * @param unit         时间单位
     * @param initialDelay 首次执行任务的延时时间
     * @param period       两次任务的间隔时间(单位秒)
     * @param task         具体待执行的任务
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(TimeUnit unit, long initialDelay,
                                                            long period, Runnable task) {
        return taskScheduler.scheduleWithFixedDelay(task, initialDelay, period, unit);
    }

    /**
     * 在指定的时间点启动，两次任务间保持固定的时间间隔
     *
     * @param unit      时间单位
     * @param startTime 首次运行的时间点,支持 "yyyy-MM-dd HH:mm:ss" 格式
     * @param period    两次任务的间隔时间
     * @param task      具体待执行的任务
     */
    public static void scheduleWithFixedDelay(TimeUnit unit, String startTime, long period, Runnable task) {
        Date dt = DateUtil.parse(startTime);
        scheduleWithFixedDelay(unit, dt, period, task);
    }

    /**
     * 在指定的时间点启动，两次任务间保持固定的时间间隔
     *
     * @param unit      时间单位
     * @param startTime 首次运行的时间点
     * @param period    两次任务的间隔时间
     * @param task      具体待执行的任务
     */
    public static void scheduleWithFixedDelay(final TimeUnit unit, Date startTime,
                                              final long period, final Runnable task) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                taskScheduler.scheduleWithFixedDelay(task, 0, period, unit);
                timer.cancel();
                timerList.remove(timer);
            }
        }, startTime);
        timerList.add(timer);
    }

    /**
     * 调整线程池大小
     */
    public static void resizeThreadPool(int threadPoolSize) {
        taskScheduler.setCorePoolSize(threadPoolSize);
    }

    /**
     * 返回定时任务线程池，可做更高级的应用
     */
    public static ScheduledThreadPoolExecutor getTaskScheduler() {
        return taskScheduler;
    }

    /**
     * 关闭定时任务服务
     * <p>系统关闭时可调用此方法终止正在执行的定时任务，一旦关闭后不允许再向线程池中添加任务，否则会报RejectedExecutionException异常</p>
     */
    public static void close() {
        int timerNum = timerList.size();
        //清除Timer
        synchronized (timerList) {
            for (Timer t : timerList)
                t.cancel();
            timerList.clear();
        }

        List<Runnable> awaitingExecution = taskScheduler.shutdownNow();
        log.info("Tasks stopping. Tasks awaiting execution: %d", timerNum + awaitingExecution.size());
    }

    /**
     * 重启动定时任务服务
     */
    public static void reset() {
        close();
        taskScheduler = new ScheduledThreadPoolExecutor(getBestPoolSize());
    }

    /**
     * 根据 Java 虚拟机可用处理器数目返回最佳的线程数。<br>
     * 最佳的线程数 = CPU可用核心数 / (1 - 阻塞系数)，其中阻塞系数这里设为0.9
     */
    private static int getBestPoolSize() {
        try {
            // JVM可用处理器的个数
            final int cores = Runtime.getRuntime().availableProcessors();
            // 最佳的线程数 = CPU可用核心数 / (1 - 阻塞系数)
            return (int) (cores / (1 - 0.9));
        } catch (Throwable e) {
            // 异常发生时姑且返回10个任务线程池
            return 10;
        }
    }
}