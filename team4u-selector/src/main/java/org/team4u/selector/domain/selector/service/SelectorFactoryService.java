package org.team4u.selector.domain.selector.service;

import org.team4u.base.lang.IdObjectService;
import org.team4u.selector.domain.selector.entity.SelectorFactory;

/**
 * 选择执行器工厂服务
 *
 * @author jay.wu
 */
public class SelectorFactoryService extends IdObjectService<String, SelectorFactory> {

    public SelectorFactoryService() {
        super(SelectorFactory.class);
    }
}