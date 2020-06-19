package org.team4u.sc;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.kv.KeyValueCleaner;
import org.team4u.kv.infrastruture.repository.memory.InMemoryKeyValueRepository;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static org.team4u.sc.TestUtil.TEST_ID;

/**
 * 哨兵缓存服务测试
 *
 * @author jay.wu
 */
public class SentinelCacheServiceTest {

    /**
     * 测试获取正常的Locker执行结果
     */
    @Test
    public void resultOfLocker() throws Throwable {
        SentinelCacheService service = newSentinelCacheService();
        Object x = service.invoke(newInvokeCommand(new FakeInvoker(), 1000, 2000));
        Assert.assertEquals(new TestDemo().setCode(TEST_ID), x);
    }

    /**
     * 测试正常的从缓存获得结果
     */
    @Test
    public void resultOfCache() throws Throwable {
        SentinelCacheService service = newSentinelCacheService();
        FakeInvoker invoker = new FakeInvoker();

        Object x1 = service.invoke(newInvokeCommand(invoker, 2000, 2000));
        // 改变执行者结果
        invoker.setTestDemo(new TestDemo().setCode(TEST_ID + TEST_ID));

        Object x2 = service.invoke(newInvokeCommand(invoker, 2000, 2000));
        // 结果不变
        Assert.assertEquals(x1, x2);
    }

    /**
     * 测试缓存超时后获取结果场景
     */
    @Test
    public void expiredResultOfCache() throws Throwable {
        SentinelCacheService service = newSentinelCacheService();
        FakeInvoker invoker = new FakeInvoker();

        Object x1 = service.invoke(newInvokeCommand(invoker, 100, 100));
        Assert.assertEquals(new TestDemo().setCode(TEST_ID), x1);

        // 改变执行者结果
        invoker.setTestDemo(new TestDemo().setCode(TEST_ID + TEST_ID));
        ThreadUtil.sleep(500);

        Object x2 = service.invoke(newInvokeCommand(invoker, 100, 100));
        // 缓存过期，结果改变
        Assert.assertEquals(new TestDemo().setCode(TEST_ID + TEST_ID), x2);
    }

    /**
     * 测试执行器返回系统异常结果场景
     */
    @Test
    public void resultWithErrorCode() throws Throwable {
        // 使用FakeSystemErrorDecider进行系统异常决策，只要执行结果与设定错误码一致即判定为系统异常
        SentinelCacheService service = newSentinelCacheService();
        FakeInvoker invoker = new FakeInvoker();

        // 设定为系统异常情况
        invoker.setTestDemo(new TestDemo().setCode(TEST_ID + TEST_ID));
        Object x1 = service.invoke(
                newInvokeCommand(
                        invoker,
                        1000,
                        2000,
                        TEST_ID + TEST_ID
                )
        );
        Assert.assertEquals(new TestDemo().setCode(TEST_ID + TEST_ID), x1);

        // 改变执行者结果
        invoker.setTestDemo(new TestDemo().setCode(TEST_ID));
        Object x2 = service.invoke(newInvokeCommand(invoker, 1000, 2000));

        // 上一次执行时系统异常，不缓存结果，无论缓存是否过期
        Assert.assertEquals(new TestDemo().setCode(TEST_ID), x2);
    }

    /**
     * 测试正常等待缓存结果场景
     */
    @Test
    public void waitingResultOfCache() throws Throwable {
        SentinelCacheService service = newSentinelCacheService();
        // 模拟执行者耗时400ms情况
        FakeInvoker invoker = new FakeInvoker(400);

        // 两个线程各自同时竞争，仅一个线程获得锁，另一个需要等待缓存结果
        AtomicReference<Object> x1 = new AtomicReference<>();
        Thread t1 = startInvokeThread(service, invoker, 2000, 1000, x1, "x1");

        AtomicReference<Object> x2 = new AtomicReference<>();
        Thread t2 = startInvokeThread(service, invoker, 2000, 1000, x2, "x2");

        // 等待两个线程执行完毕
        t1.join();
        t2.join();

        // 最终两个线程结果应该一致
        Assert.assertEquals(x1.get(), x2.get());
    }

    /**
     * 测试等待缓存结果超时场景
     */
    @Test
    public void waitingResultOfCacheTimeout() throws Throwable {
        SentinelCacheService service = newSentinelCacheService();
        // 模拟执行者耗时500ms情况
        FakeInvoker invoker = new FakeInvoker(500);

        // 两个线程各自同时竞争，仅一个线程获得锁，另一个需要等待缓存结果
        // 因锁有效时间小于执行时间，等待线程必定超时
        AtomicReference<Object> x1 = new AtomicReference<>();
        Thread t1 = startInvokeThread(service, invoker, 2000, 100, x1, "x1");

        AtomicReference<Object> x2 = new AtomicReference<>();
        Thread t2 = startInvokeThread(service, invoker, 2000, 100, x2, "x2");

        // 等待两个线程执行完毕
        t1.join();
        t2.join();

        // 获取哨兵执行结果判断
        Object x = ObjectUtil.defaultIfNull(x1.get(), x2.get());
        Assert.assertEquals(new TestDemo().setCode(TEST_ID), x);

        // 等待线程超时，无法获得结果
        Assert.assertTrue(x2.get() == null || x1.get() == null);
    }

    /**
     * 启动执行线程
     */
    private Thread startInvokeThread(SentinelCacheService service,
                                     Invoker invoker,
                                     int resultTtlMillis,
                                     int lockTtlMillis,
                                     AtomicReference<Object> result,
                                     String threadName) {
        Thread t = ThreadUtil.newThread(() -> {
            try {
                result.set(service.invoke(newInvokeCommand(invoker, resultTtlMillis, lockTtlMillis)));
            } catch (TimeoutException e) {
                // 正常超时
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                Assert.fail();
            }
        }, threadName);

        t.start();

        return t;
    }

    private InvokeCommand newInvokeCommand(Invoker invoker,
                                           int resultTtlMillis,
                                           int lockMillis,
                                           String... errorCodes) {
        return new InvokeCommand()
                .setId(TEST_ID)
                .setInvoker(invoker)
                .setResultClass(TestDemo.class)
                .setResultTtlMillis(resultTtlMillis)
                .setLockTtlMillis(lockMillis)
                .setResultCodeKey(TEST_ID)
                .setSystemErrorCodes(errorCodes)
                .setSystemErrorDecider(new FakeSystemErrorDecider());
    }

    private SentinelCacheService newSentinelCacheService() {
        KeyValueCleaner clear = new KeyValueCleaner(new KeyValueCleaner.Config(), null);
        return new SentinelCacheService(new InMemoryKeyValueRepository(), clear);
    }

}