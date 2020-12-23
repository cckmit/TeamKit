package org.team4u.workflow.infrastructure.instance;

import org.team4u.ddd.domain.model.DomainEventAwareRepository;
import org.team4u.ddd.event.EventStore;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的流程实例资源库
 *
 * @author jay.wu
 */
public class InMemoryProcessInstanceRepository
        extends DomainEventAwareRepository<ProcessInstance>
        implements ProcessInstanceRepository {

    private final Map<String, ProcessInstance> instanceMap = new ConcurrentHashMap<>();

    public InMemoryProcessInstanceRepository(EventStore eventStore) {
        super(eventStore);
    }

    @Override
    public ProcessInstance domainOf(String domainId) {
        return instanceMap.get(domainId);
    }

    @Override
    protected void doSave(ProcessInstance domain) {
        instanceMap.put(domain.getProcessInstanceId(), domain);
    }
}