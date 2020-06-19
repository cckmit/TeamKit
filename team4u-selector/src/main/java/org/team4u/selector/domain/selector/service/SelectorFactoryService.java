package org.team4u.selector.domain.selector.service;

import org.team4u.selector.domain.selector.entity.SelectorFactory;
import org.team4u.core.lang.IdObjectService;

import java.util.ServiceLoader;

/**
 * 选择执行器工厂服务
 *
 * @author jay.wu
 */
public class SelectorFactoryService extends IdObjectService<String, SelectorFactory> {

    public SelectorFactoryService() {
        ServiceLoader<SelectorFactory> serviceLoader = ServiceLoader.load(SelectorFactory.class);

        for (SelectorFactory selectorFactory : serviceLoader) {
            saveIdObject(selectorFactory);
        }
    }
}