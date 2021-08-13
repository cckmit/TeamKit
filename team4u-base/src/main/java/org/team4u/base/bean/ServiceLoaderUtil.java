package org.team4u.base.bean;

import cn.hutool.log.Log;
import cn.hutool.log.level.Level;
import org.team4u.base.log.LogMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.stream.Collectors;

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
        LogMessage lm = LogMessage.create(ServiceLoaderUtil.class.getSimpleName(), "loadAvailableList");

        Iterator<T> iterator = load(clazz).iterator();
        List<T> result = new ArrayList<>();

        while (iterator.hasNext()) {
            try {
                result.add(iterator.next());
            } catch (ServiceConfigurationError e) {
                String errorLm = lm.copy().fail(e.getMessage()).toString();
                if (log.isEnabled(Level.DEBUG)) {
                    log.debug(e, errorLm);
                } else {
                    log.info(errorLm);
                }
            }
        }

        log.info(lm.success()
                .append("result", result.stream()
                        .map(it -> it.getClass().getName())
                        .collect(Collectors.toList()))
                .toString());
        return result;
    }
}