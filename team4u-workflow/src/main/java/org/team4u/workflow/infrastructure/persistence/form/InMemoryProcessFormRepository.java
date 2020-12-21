package org.team4u.workflow.infrastructure.persistence.form;

import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.form.ProcessFormRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的流程表单资源库
 *
 * @author jay.wu
 */
public class InMemoryProcessFormRepository<F extends ProcessForm>
        implements ProcessFormRepository<F> {

    private final Map<String, F> forms = new ConcurrentHashMap<>();

    @Override
    public F formOf(String formId) {
        return forms.get(formId);
    }

    @Override
    public void save(F form) {
        forms.put(form.getFormId(), form);
    }
}