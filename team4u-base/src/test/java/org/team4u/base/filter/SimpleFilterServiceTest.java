package org.team4u.base.filter;

import cn.hutool.core.lang.Dict;

public class SimpleFilterServiceTest extends AbstractSimpleFilterServiceTest {

    @Override
    protected SimpleFilterService<Dict> filterService() {
        return new SimpleFilterService<>(filters);
    }
}