package org.team4u.base.lang;

import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;

/**
 * SPI机制中的服务加载工具类
 *
 * @author jay.wu
 * @see cn.hutool.core.util.ServiceLoaderUtil
 */
public class ServiceLoaderUtil extends cn.hutool.core.util.ServiceLoaderUtil {

    private static final Log log = Log.get();

    /**
     * 加载可用的服务列表
     *
     * @param clazz 服务接口
     * @param <T>   接口类型
     * @return 可用的服务列表
     */
    public static <T> List<T> loadAvailableList(Class<T> clazz) {
        Iterator<T> iterator = load(clazz).iterator();
        List<T> result = new ArrayList<>();

        while (iterator.hasNext()) {
            try {
                result.add(iterator.next());
            } catch (ServiceConfigurationError ignore) {
                // ignore
            }
        }

        log.info(LogMessage.create(ServiceLoaderUtil.class.getSimpleName(), "loadAvailableList")
                .success()
                .append("result", result)
                .toString());
        return result;
    }
}