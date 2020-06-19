package org.team4u.selector.domain.config.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.team4u.selector.domain.config.entity.SelectorConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 复合选择器配置资源库
 *
 * @author jay.wu
 */
public class CompositeSelectorConfigRepository implements SelectorConfigRepository {

    private List<SelectorConfigRepository> repositories;

    public CompositeSelectorConfigRepository(SelectorConfigRepository... repositories) {
        this(CollUtil.toList(repositories));
    }

    public CompositeSelectorConfigRepository(List<SelectorConfigRepository> repositories) {
        this.repositories = ObjectUtil.defaultIfNull(repositories, new ArrayList<>());
    }

    @Override
    public SelectorConfig selectorConfigOfId(String id) {
        for (SelectorConfigRepository repository : repositories) {
            SelectorConfig selectorConfig = repository.selectorConfigOfId(id);
            if (selectorConfig != null) {
                return selectorConfig;
            }
        }

        return null;
    }
}