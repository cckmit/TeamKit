package org.team4u.base.filter;

import cn.hutool.core.lang.Dict;

public class FilterServiceTest extends AbstractFilterServiceTest {

    @Override
    protected FilterService<Dict> filterService() {
        return new FilterService<>(filters);
    }
}