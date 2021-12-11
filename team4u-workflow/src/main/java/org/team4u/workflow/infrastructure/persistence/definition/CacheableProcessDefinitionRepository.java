package org.team4u.workflow.infrastructure.persistence.definition;

import cn.hutool.cache.CacheUtil;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;

/**
 * 可缓存的流程定义资源库
 *
 * @author jay.wu
 */
public class CacheableProcessDefinitionRepository implements ProcessDefinitionRepository {

    private final LazyFunction<String, ProcessDefinition> lazyDefinition;

    private final ProcessDefinitionRepository delegate;

    public CacheableProcessDefinitionRepository(ProcessDefinitionRepository delegate,
                                                int timeoutMillis) {
        this.delegate = delegate;

        lazyDefinition = LazyFunction.of(
                LazyFunction.Config.builder()
                        .name(getClass().getSimpleName() + "|lazyDefinition")
                        .cache(CacheUtil.newTimedCache(timeoutMillis))
                        .build(),
                this::domainOf
        );
    }

    @Override
    public ProcessDefinition domainOf(String domainId) {
        return lazyDefinition.apply(domainId);
    }

    @Override
    public void save(ProcessDefinition domain) {
        delegate.save(domain);
        // 保存后及时更新缓存
        lazyDefinition.removeIf(it -> it.getProcessDefinitionId().toString());
    }
}