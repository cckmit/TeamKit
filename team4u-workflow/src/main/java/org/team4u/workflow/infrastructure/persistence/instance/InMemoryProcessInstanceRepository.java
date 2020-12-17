package org.team4u.workflow.infrastructure.persistence.instance;

import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的流程实例资源库
 *
 * @author jay.wu
 */
public class InMemoryProcessInstanceRepository implements ProcessInstanceRepository {

    private final Map<String, ProcessInstance> instanceMap = new ConcurrentHashMap<>();

    @Override
    public ProcessInstance domainOf(String domainId) {
        return instanceMap.get(domainId);
    }

    @Override
    public void save(ProcessInstance domain) {
        instanceMap.put(domain.getProcessInstanceId(), domain);
    }
}