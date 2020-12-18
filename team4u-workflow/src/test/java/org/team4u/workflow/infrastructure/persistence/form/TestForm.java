package org.team4u.workflow.infrastructure.persistence.form;

import org.team4u.workflow.domain.form.ProcessForm;

public class TestForm extends ProcessForm {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
