package org.team4u.workflow.infrastructure.form;

import org.team4u.workflow.domain.form.FormIndex;
import org.team4u.workflow.domain.form.FormIndexRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的表单索引资源库
 *
 * @author jay.wu
 */
public class InMemoryFormIndexRepository<F extends FormIndex>
        implements FormIndexRepository<F> {

    private final Map<String, F> formIndex = new ConcurrentHashMap<>();

    @Override
    public F formOf(String instanceId) {
        return formIndex.get(instanceId);
    }

    @Override
    public void save(F formIndex) {
        this.formIndex.put(formIndex.getProcessInstanceId(), formIndex);
    }
}