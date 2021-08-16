package org.team4u.selector.domain.selector;

import org.team4u.base.lang.IdObjectService;

/**
 * 选择执行器工厂服务
 *
 * @author jay.wu
 */
public class SelectorFactoryService extends IdObjectService<String, SelectorFactory> {

    public SelectorFactoryService() {
        super(SelectorFactory.class);

        saveObjectsByBeanProvidersAndEvent();
    }
}