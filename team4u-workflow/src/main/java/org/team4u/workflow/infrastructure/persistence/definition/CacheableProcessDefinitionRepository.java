package org.team4u.workflow.infrastructure.persistence.definition;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import org.team4u.base.lang.CacheableFunc1;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;

/**
 * 可缓存的流程定义资源库
 *
 * @author jay.wu
 */
public class CacheableProcessDefinitionRepository implements ProcessDefinitionRepository {

    private final CacheFunc cacheFunc;
    private final ProcessDefinitionRepository delegate;

    public CacheableProcessDefinitionRepository(ProcessDefinitionRepository delegate,
                                                int timeoutMillis) {
        this.delegate = delegate;
        this.cacheFunc = new CacheFunc(CacheUtil.newTimedCache(timeoutMillis));
    }

    @Override
    public ProcessDefinition domainOf(String domainId) {
        return cacheFunc.callWithCache(domainId);
    }

    @Override
    public void save(ProcessDefinition domain) {
        delegate.save(domain);
        // 保存后及时更新缓存
        cacheFunc.removeCache(domain.getProcessDefinitionId().toString());
    }

    private class CacheFunc extends CacheableFunc1<String, ProcessDefinition> {

        public CacheFunc(Cache<String, ProcessDefinition> cache) {
            super(cache);
        }

        @Override
        public ProcessDefinition call(String id) {
            return domainOf(id);
        }
    }
}