package org.team4u.workflow.domain.form.process.node.handler;

import org.team4u.base.lang.IdObjectService;

public class AssigneeRepositories extends IdObjectService<String, AssigneeRepository> {

    public AssigneeRepositories() {
        super(AssigneeRepository.class);
    }
}