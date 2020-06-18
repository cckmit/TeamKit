package org.team4u.core.lang;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.VoidFunc1;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.core.error.NoAvailableResourceException;
import org.team4u.core.log.LogMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询调度调度策略
 *
 * @param <T> 资源类型
 * @author jay.wu
 */
public class RoundRobinPolicy<T> {

    private final Log log = LogFactory.get();

    protected List<T> resources;
    protected AtomicInteger currentIndex = new AtomicInteger(0);

    public RoundRobinPolicy() {
    }

    /**
     * @param resources 资源集合
     */
    public RoundRobinPolicy(List<T> resources) {
        this.resources = new ArrayList<T>(resources);
    }

    /**
     * 以轮询的方式依次将请求调度不同的资源，即每次调度执行i = (i + 1) mod n，并选出第i个资源
     *
     * @return 资源对象
     */
    public T select() {
        return select(resources);
    }

    public T select(List<T> resources) {
        int index = getCurrentResourceIndex(resources);
        return resources.get(index % resources.size());
    }

    public void request(VoidFunc1<T> action) {
        request(resources, action);
    }

    /**
     * 以轮询的方式依次将请求调度不同的资源，即每次调度执行i = (i + 1) mod n，并选出第i个资源
     * 若某个资源执行失败,将继续尝试其余资源,若所有资源都执行失败,则抛出异常
     *
     * @param resources 资源集合
     * @param action    资源处理类
     */
    public void request(List<T> resources, final VoidFunc1<T> action) {
        request(resources, new Func1<T, Void>() {
            @Override
            public Void call(T obj) throws Exception {
                action.call(obj);
                return null;
            }
        });
    }

    public <V> V request(Func1<T, V> action) {
        return request(resources, action);
    }

    /**
     * 以轮询的方式依次将请求调度不同的资源，即每次调度执行i = (i + 1) mod n，并选出第i个资源
     * 若某个资源执行失败,将继续尝试其余资源,若所有资源都执行失败,则抛出异常
     *
     * @param resources 资源集合
     * @param action    资源处理类
     */
    public <V> V request(List<T> resources, Func1<T, V> action) {
        int index = getCurrentResourceIndex(resources);

        for (int i = 0; i < resources.size(); i++) {
            T resource = resources.get((index + i) % resources.size());
            try {
                return action.call(resource);
            } catch (Exception e) {
                log.warn(e, LogMessage.create(this.getClass().getSimpleName(), "request")
                        .fail()
                        .append("resource", resource)
                        .toString());
            }
        }

        throw new NoAvailableResourceException();
    }

    private int getCurrentResourceIndex(List<T> resources) {
        synchronized (this) {
            if (currentIndex.get() == Integer.MAX_VALUE) {
                currentIndex.set(0);
            }
        }

        return Math.abs(currentIndex.getAndIncrement()) % resources.size();
    }

    public List<T> getResources() {
        return resources;
    }
}