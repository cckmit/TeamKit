package org.team4u.selector.domain.selector;

import org.team4u.base.lang.IdObjectService;

import java.util.List;

/**
 * 选择器值服务
 *
 * @author jay.wu
 */
public class SelectorValueService extends IdObjectService<String, SelectorValueHandler> {

    public SelectorValueService() {
        super();

        saveObjectByBeanInitializedEvent();
    }

    public SelectorValueService(Class<SelectorValueHandler> valueClass) {
        super(valueClass);

        saveObjectByBeanInitializedEvent();
    }

    public SelectorValueService(List<SelectorValueHandler> objects) {
        super(objects);
    }
}